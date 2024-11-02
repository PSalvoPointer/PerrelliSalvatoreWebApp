console.log('HELLO JS');

// Evento click sul pulsante "Aggiungi un nuovo libro"
document.getElementById("addBookButton").addEventListener("click", function() {
    document.getElementById("bookForm").style.display = "block";
    document.getElementById("removeBookForm").style.display = "none";
});

// Evento submit sul form "newBookForm"
document.getElementById("newBookForm").addEventListener("submit", function(event) {
    event.preventDefault();

    const title = document.getElementById("title").value;
    const year = document.getElementById("year").value;
    const publisher = document.getElementById("publisher").value;
    const tableBody = document.getElementById("bookTable").getElementsByTagName('tbody')[0];

    addNewRow(tableBody, title, year, publisher);

    document.getElementById("bookForm").style.display = "none";
    document.getElementById("newBookForm").reset();
});

function addNewRow(tableBody, title, year, publisher) {
    const newRow = tableBody.insertRow();
    const titleCell = newRow.insertCell(0);
    const yearCell = newRow.insertCell(1);
    const publisherCell = newRow.insertCell(2);
    const deleteButtonCell = newRow.insertCell(3);

    titleCell.textContent = title;
    yearCell.innerHTML = `<span class="badge bg-primary">${year}</span>`;
    publisherCell.textContent = publisher;
    deleteButtonCell.innerHTML = `<button class="btn btn-danger btn-sm deleteButton"><i class="fa-solid fa-trash"></i></button>`;

    // Aggiungi l'evento per il pulsante elimina
    deleteButtonCell.querySelector(".deleteButton").addEventListener("click", function() {
        newRow.remove();
    });
}
