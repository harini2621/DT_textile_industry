const API_URL = "http://localhost:8081/api/workers";

document.addEventListener("DOMContentLoaded", () => {
    loadWorkers();

    document.getElementById("workerForm").addEventListener("submit", saveWorker);
});

function loadWorkers() {
    fetch(API_URL)
        .then(response => response.json())
        .then(data => {
            const tableBody = document.getElementById("workerTableBody");
            tableBody.innerHTML = "";

            data.forEach(worker => {
                const row = `
                    <tr>
                        <td>${worker.id}</td>
                        <td>${worker.name}</td>
                        <td>${worker.department}</td>
                        <td>${worker.role}</td>
                        <td>${worker.phone}</td>
                        <td>
                            <button onclick="deleteWorker(${worker.id})">Delete</button>
                        </td>
                    </tr>
                `;
                tableBody.innerHTML += row;
            });
        })
        .catch(error => console.error("Error loading workers:", error));
}

function saveWorker(event) {
    event.preventDefault();

    const worker = {
        name: document.getElementById("name").value,
        department: document.getElementById("department").value,
        role: document.getElementById("role").value,
        phone: document.getElementById("phone").value
    };

    fetch(API_URL, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(worker)
    })
    .then(() => {
        document.getElementById("workerForm").reset();
        loadWorkers();
    })
    .catch(error => console.error("Error saving worker:", error));
}

function deleteWorker(id) {
    if (confirm("Are you sure you want to delete this worker?")) {
        fetch(`${API_URL}/${id}`, {
            method: "DELETE"
        })
        .then(() => loadWorkers())
        .catch(error => console.error("Error deleting worker:", error));
    }
}