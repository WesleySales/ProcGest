document.addEventListener("DOMContentLoaded", function () {
    const dropZone = document.getElementById("drop-zone");
    const fileInput = document.getElementById("file-input");
    const dropText = document.getElementById("drop-text");

    function mostrarArquivoSelecionado(arquivo) {
        if (arquivo) {
            dropText.textContent = `Arquivo selecionado: ${arquivo.name}`;
            dropZone.style.borderColor = "#4CAF50"; // verde
        }
    }

    dropZone.addEventListener("click", () => fileInput.click());

    dropZone.addEventListener("dragover", (e) => {
        e.preventDefault();
        dropZone.style.borderColor = "#333";
    });

    dropZone.addEventListener("dragleave", () => {
        dropZone.style.borderColor = "#999";
    });

    dropZone.addEventListener("drop", (e) => {
        e.preventDefault();
        dropZone.style.borderColor = "#999";

        if (e.dataTransfer.files.length) {
            fileInput.files = e.dataTransfer.files;
            mostrarArquivoSelecionado(fileInput.files[0]);
        }
    });

    // Detecta quando o arquivo é escolhido manualmente
    fileInput.addEventListener("change", () => {
        if (fileInput.files.length) {
            mostrarArquivoSelecionado(fileInput.files[0]);
        }
    });
});
