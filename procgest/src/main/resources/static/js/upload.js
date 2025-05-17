document.getElementById("upload-btn").addEventListener("click", async () => {
    const file = fileInput.files[0];
    const token = localStorage.getItem("token");

    if (!file) {
        alert("Selecione um arquivo antes de enviar.");
        return;
    }

    const formData = new FormData();
    formData.append("file", file); // "file" deve ser o mesmo nome aceito no seu controller

    try {
        const response = await fetch("http://localhost:8080/procuracoes/upload", {
            method: "POST",
            headers: {
                "Authorization": `Bearer ${token}` // só o token vai no header, FormData cuida do resto
            },
            body: formData
        });

        if (response.ok) {
            alert("Upload realizado com sucesso!");
        } else {
            alert("Erro ao enviar arquivo: " + response.status);
        }
    } catch (error) {
        console.error("Erro na requisição:", error);
        alert("Erro ao conectar com o servidor.");
    }
});