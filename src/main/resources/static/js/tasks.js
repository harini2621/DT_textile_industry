const API_URL = "http://localhost:8081/api/tasks";

document.addEventListener("DOMContentLoaded", () => {
    loadTasks();

    document.getElementById("taskForm").addEventListener("submit", saveTask);
});

function loadTasks() {
    fetch(API_URL)
        .then(response => response.json())
        .then(data => {
            const tableBody = document.getElementById("taskTableBody");
            tableBody.innerHTML = "";

            data.forEach(task => {
                const row = `
                    <tr>
                        <td>${task.id}</td>
                        <td>${task.taskName}</td>
                        <td>${task.assignedWorker}</td>
                        <td>${task.dueDate}</td>
                        <td>${task.status}</td>
                        <td>
                            <button onclick="deleteTask(${task.id})">Delete</button>
                        </td>
                    </tr>
                `;
                tableBody.innerHTML += row;
            });
        })
        .catch(error => console.error("Error loading tasks:", error));
}

function saveTask(event) {
    event.preventDefault();

    const task = {
        taskName: document.getElementById("taskName").value,
        assignedWorker: document.getElementById("assignedWorker").value,
        dueDate: document.getElementById("dueDate").value,
        status: document.getElementById("status").value
    };

    fetch(API_URL, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(task)
    })
    .then(() => {
        document.getElementById("taskForm").reset();
        loadTasks();
    })
    .catch(error => console.error("Error saving task:", error));
}

function deleteTask(id) {
    if (confirm("Are you sure you want to delete this task?")) {
        fetch(`${API_URL}/${id}`, {
            method: "DELETE"
        })
        .then(() => loadTasks())
        .catch(error => console.error("Error deleting task:", error));
    }
}