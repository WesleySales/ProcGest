// Espera o DOM estar carregado para evitar erros com elementos ainda não existentes
//document.addEventListener("DOMContentLoaded", () => {
    const form = document.getElementById('loginForm');
    const mensagem = document.getElementById('mensagem');


    // Verifica se os elementos realmente existem
//    if (!form) {
//        console.error("Formulário não encontrado.");
//        return;
//    }

    form.addEventListener('submit', async (event) => {
        event.preventDefault();

        const login = document.getElementById('login')?.value;
        const senha = document.getElementById('senha')?.value;

        verificarCampos();

        try {
            const response = await fetch('http://localhost:8080/auth/login', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ login, senha })
            });

            if (response.ok) {
                const data = await response.json();
                const token = data.token;

                localStorage.setItem("token", token);
//                alert("Login realizado com sucesso. Token: " + token);

//                if (mensagem) {
//                    mensagem.textContent = token;
//                }

//                alert("Redirecionando...");
//                 redireciona para outra página
                window.location.href = "home.html";
            } else {
                alert("Login inválido. Verifique suas credenciais.");
            }

        } catch (error) {
            console.error("Erro ao conectar com o servidor:", error);
            if (mensagem) {
                mensagem.textContent = "Erro ao conectar com o servidor.";
            }
        }
    });
//});

    function verificarCampos(){
        if (!login || !senha) {
            alert("Preencha todos os campos.");
            return;
        }
    }
