const API_URL = "http://localhost:8081/api/reports";

document.addEventListener("DOMContentLoaded", () => {
    loadReports();

    document.getElementById("reportForm").addEventListener("submit", saveReport);
});

function loadReports() {
    fetch(API_URL)
        .then(response => response.json())
        .then(data => {
            const tableBody = document.getElementById("reportTableBody");
            tableBody.innerHTML = "";

            data.forEach(report => {
                const row = `
                    <tr>
                        <td>${report.id}</td>
                        <td>${report.reportType}</td>
                        <td>${report.generatedBy}</td>
                        <td>${report.description}</td>
                        <td>
                            <button onclick="deleteReport(${report.id})">
                                Delete
                            </button>
                        </td>
                    </tr>
                `;
                tableBody.innerHTML += row;
            });
        })
        .catch(error => console.error("Error loading reports:", error));
}

function saveReport(event) {
    event.preventDefault();

    const report = {
        reportType: document.getElementById("reportType").value,
        generatedBy: document.getElementById("generatedBy").value,
        description: document.getElementById("description").value
    };

    fetch(API_URL, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(report)
    })
    .then(() => {
        document.getElementById("reportForm").reset();
        loadReports();
    })
    .catch(error => console.error("Error saving report:", error));
}

function deleteReport(id) {
    if (confirm("Are you sure you want to delete this report?")) {
        fetch(`${API_URL}/${id}`, {
            method: "DELETE"
        })
        .then(() => loadReports())
        .catch(error => console.error("Error deleting report:", error));
    }
}