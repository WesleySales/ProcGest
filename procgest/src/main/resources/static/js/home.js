const token = localStorage.getItem("token");

async function verificarUsuarioLogado() {
    if (!token) {
        alert("Usuário não autenticado.\nREDIRECIONANDO PARA PÁGINA DE LOGIN...");
        window.location.href = "login.html";
        return false;
    }
    return true;
}
//document.addEventListener('DOMContentLoaded', verificarUsuarioLogado);
async function carregarEstatisticas(){
        try{
            const response = await fetch('http://localhost:8080/procuracoes/estatisticas', {
                            method: 'GET',
                            headers: {
                                "Authorization": `Bearer ${token}`,
                            },
                        });
            if (!response.ok) throw new Error("Erro ao buscar  as procurações");

            const estatisticas = await response.json();

            document.getElementById("totalProcuracoes").textContent = estatisticas["Total de Procurações"];
            document.getElementById("pendentes").textContent = estatisticas["Procurações Pendentes"] ;
            document.getElementById("expiradas").textContent = estatisticas["Procurações Expiradas"];
            document.getElementById("proximas").textContent = estatisticas["Procurações a Vencer nos proximos 30 dias"] ;

        } catch (error) {
                console.error("Erro ao carregar estatísticas:", error);
                alert("Erro ao carregar estatísticas: " + error.message);
        }
}



async function carregarProcuracoes() {
        try {
            const response = await fetch('http://localhost:8080/procuracoes', {
                            method: 'GET',
                            headers: {
                                "Authorization": `Bearer ${token}`,
                            },
                        });
            if (!response.ok) throw new Error("Erro ao buscar  as procurações");

            const procuracoes = await response.json();

            console.log(procuracoes); // Veja a estrutura real

            const tabelaBody = document.querySelector("#tabela-procuracoes tbody");
            tabelaBody.innerHTML = ""; // limpa a tabela

            procuracoes.forEach(proc => {
                const linha = document.createElement("tr");
                linha.innerHTML = `
                    <td>${proc.nomeProcurador || "-"}</td>
                    <td>${proc.diasParaVencer || "-"}</td>
                    <td>${proc.status || "-"}</td>
                    <td>${proc.dataInicio || "-"}</td>
                    <td>${proc.dataVencimento || "-"}</td>
                    <td>EDITAR</td>
                    <td>DELETAR</td>
                `;
                tabelaBody.appendChild(linha);
            });
        } catch (error) {
            alert("Erro ao carregar os dados: " + error.message);
            console.error(error);
        }
    }

async function carregarDadosUsuario() {
      try {
        const response = await fetch('http://localhost:8080/auth/carregarDadosUsuario', {
                            method: 'GET',
                            headers: {
                                "Authorization": `Bearer ${token}`,
                            },
                        });
        if (!response.ok) {
          throw new Error('Não autorizado');
        }

        const usuario = await response.json();

        document.getElementById('nomeUsuario').textContent = "BEM VINDO, "+usuario.login.toUpperCase()+"!";

      } catch (error) {
        console.error('Erro ao carregar dados do usuário:', error);
      }
}

async function encerrarSessao() {

    try {
        await fetch('http://localhost:8080/auth/logout', {
            method: 'POST',
            headers: {
                'Authorization': `Bearer ${token}`
            }
        });

    } catch (error) {
        console.error("Erro no logout:", error);
    } finally {
        localStorage.removeItem('token');

        window.location.href = 'login.html';

        window.history.replaceState(null, null, 'login.html');
    }
}


async function inicializarPagina() {
    const estaLogado = await verificarUsuarioLogado();

    if (estaLogado) {
        await Promise.all([
            carregarDadosUsuario(),
            carregarProcuracoes(),
            carregarEstatisticas()
        ]);
    }
}

document.addEventListener('DOMContentLoaded', inicializarPagina);
//document.addEventListener('DOMContentLoaded', verificarUsuarioLogado);



