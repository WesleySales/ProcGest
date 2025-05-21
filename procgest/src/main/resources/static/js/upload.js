document.addEventListener("DOMContentLoaded", function () {
    // Elementos da página
    const dropZone = document.getElementById("drop-zone");
    const fileInput = document.getElementById("file-input");
    const dropText = document.getElementById("drop-text");
    const formUpload = document.getElementById("upload-form");
    const formCadastroProcuracao = document.getElementById('formCadastroProcuracao');
    const token = localStorage.getItem("token");
    const sidebar = document.getElementById('sidebar');
    const toggleBtn = document.querySelector('.toggle-btn');

    // Função para alternar a barra lateral
    function toggleSidebar() {
        sidebar.classList.toggle('recolhida');
    }

    // Função para navegar entre páginas e destacar o menu ativo
    function navegar(pagina) {
        // Remove a classe 'ativo' de todos os itens do menu
        document.querySelectorAll('.sidebar a').forEach(item => {
            item.classList.remove('ativo');
        });

        // Adiciona a classe 'ativo' ao item correspondente
        const itemMenu = document.querySelector(`.sidebar a[onclick="navegar('${pagina}')"]`);
        if (itemMenu) {
            itemMenu.classList.add('ativo');
        }

        // Aqui você pode adicionar a lógica para carregar o conteúdo da página
        console.log(`Navegando para: ${pagina}`);
        // Exemplo: carregarPagina(pagina);
    }

    // Configura eventos da sidebar
    toggleBtn.addEventListener('click', toggleSidebar);

    // Configura eventos de navegação
    document.querySelectorAll('.sidebar a').forEach(link => {
        const onclickAttr = link.getAttribute('onclick');
        if (!onclickAttr) {
            link.addEventListener('click', function(e) {
                e.preventDefault();
                const pagina = this.getAttribute('data-pagina');
                navegar(pagina);
            });
        }
    });

    // Define o menu ativo inicial
    const paginaAtual = determinarPaginaAtual();
    if (paginaAtual) {
        navegar(paginaAtual);
    }

    // Clique ativa o input
    dropZone.addEventListener("click", () => {
        fileInput.click();
    });

    // Highlight quando arrasta por cima
    ["dragenter", "dragover"].forEach(eventName => {
        dropZone.addEventListener(eventName, e => {
            e.preventDefault();
            dropZone.style.backgroundColor = "#e6f7f5"; /* Turquesa muito claro */
        });
    });

    ["dragleave", "drop"].forEach(eventName => {
        dropZone.addEventListener(eventName, e => {
            e.preventDefault();
            dropZone.style.backgroundColor = "";
        });
    });

    // Drop do arquivo
    dropZone.addEventListener("drop", e => {
        e.preventDefault();
        fileInput.files = e.dataTransfer.files;
        showFileNames(fileInput.files);
    });

    // Mostra nomes dos arquivos
    fileInput.addEventListener("change", () => {
        showFileNames(fileInput.files);
    });

    function showFileNames(files) {
        const names = Array.from(files).map(file => file.name).join(", ");
        dropText.textContent = names ? `Selecionado: ${names}` : "Nenhum arquivo selecionado";
    }

    // Submit manual com fetch
    formUpload.addEventListener("submit", async (e) => {
        e.preventDefault();

        const files = fileInput.files;
        if (files.length === 0) {
            alert("Selecione ao menos um arquivo.");
            return;
        }

        const formData = new FormData();
        for (let file of files) {
            formData.append("files", file);
        }

        try {
            const response = await fetch("http://localhost:8080/procuracoes/upload", {
                method: "POST",
                headers: {
                    "Authorization": `Bearer ${token}`
                },
                body: formData
            });

            if (response.ok) {
                alert("Upload realizado com sucesso!");
                dropText.textContent = "Nenhum arquivo selecionado";
                fileInput.value = "";
            } else {
                alert("Erro ao enviar arquivos: " + response.status);
            }
        } catch (error) {
            console.error("Erro na requisição:", error);
            alert("Erro ao conectar com o servidor: " + error);
        }
    });

    formCadastroProcuracao.addEventListener("submit", async (e) => {
        e.preventDefault();

        const nomeProcurador = document.getElementById("nomeProcurador").value;
        const dataInicio = document.getElementById("dataInicio").value;
        const dataVencimento = document.getElementById("dataVencimento").value;
        const status = document.getElementById("boxStatus").value;

        const procuracaoDTO = {
            nomeProcurador: nomeProcurador,
            dataInicio: dataInicio,
            dataVencimento: dataVencimento,
            status: status
        };

        try {
            const response = await fetch('http://localhost:8080/procuracoes/cadastrar', {
                method: 'POST',
                headers: {
                    "Authorization": `Bearer ${token}`,
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(procuracaoDTO)
            });

            if (response.ok) {
                alert("CADASTRO REALIZADO COM SUCESSO!");
                formCadastroProcuracao.reset();
            } else {
                const erroTexto = await response.text();
                alert("Erro do servidor: " + erroTexto);
            }
        } catch (error) {
            console.error("Erro ao conectar com o servidor:", error);
            alert("Erro ao conectar com o servidor: " + error);
        }
    });

    // Função para determinar a página atual
    function determinarPaginaAtual() {
        const path = window.location.pathname;
        if (path.includes('procuracoes')) return 'procuracoes';
        if (path.includes('cadastrar')) return 'cadastrar';
        if (path.includes('perfil')) return 'perfil';
        return 'home';
    }

    // Torna as funções acessíveis globalmente
    window.toggleSidebar = toggleSidebar;
    window.navegar = navegar;
});