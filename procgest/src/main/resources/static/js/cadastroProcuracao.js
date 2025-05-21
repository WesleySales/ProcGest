form.addEventListener("submit", async (e) => {
    e.preventDefault();

    const nomeProcurador = document.getElementById("nomeProcurador").value;
    const dataInicio = document.getElementById("dataInicio").value;
    const dataVencimento = document.getElementById("dataVencimento").value;
    const status = document.getElementById("boxStatus").value;

    const procuracaoDTO = {
        nomeProcurador: nomeProcurador,
        dataInicio: dataInicio,          // deve ser tipo: "2024-05-21"
        dataVencimento: dataVencimento,  // idem
        status: status
    };

    alert("Salvando procuracao de " + nomeProcurador);

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
            alert("Cadastro pelo formulário deu certo");
            if (mensagem) {
                mensagem.textContent = "Cadastro concluído com sucesso!";
            }
        } else {
            const erroTexto = await response.text();
            alert("Erro do servidor: " + erroTexto);
        }

    } catch (error) {
        console.error("Erro ao conectar com o servidor:", error);
        alert("Erro ao conectar com o servidor: " + error);
    }
});
