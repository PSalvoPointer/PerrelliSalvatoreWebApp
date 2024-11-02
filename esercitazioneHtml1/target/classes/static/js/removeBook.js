// DELETE ROW ON DEMAND
document.getElementById("removeBookButton").addEventListener("click", function() {
    document.getElementById("removeBookForm").style.display = "block";
    document.getElementById("bookForm").style.display = "none";
});

// Funzione per rimuovere il libro in base al numero
document.getElementById("removeBookByNumberForm").addEventListener("submit", function(event) {
    event.preventDefault();

    const bookNumber = parseInt(document.getElementById("bookNumber").value);
    const rows = document.getElementById('bookTable').getElementsByTagName('tbody')[0].rows;

    if (bookNumber > 0 && bookNumber <= rows.length) {
        rows[bookNumber - 1].remove();
        document.getElementById("removeBookForm").style.display = "none";
        document.getElementById("removeBookByNumberForm").reset();
    } else {
        alert("Numero libro non valido!");
    }
});
