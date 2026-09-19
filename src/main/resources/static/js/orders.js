const API_URL = "http://localhost:8081/api/orders";

document.addEventListener("DOMContentLoaded", () => {
    loadOrders();

    document.getElementById("orderForm").addEventListener("submit", saveOrder);
});

function loadOrders() {
    fetch(API_URL)
        .then(response => response.json())
        .then(data => {
            const tableBody = document.getElementById("orderTableBody");
            tableBody.innerHTML = "";

            data.forEach(order => {
                const row = `
                    <tr>
                        <td>${order.orderId}</td>
                        <td>${order.orderName}</td>
                        <td>${order.productName}</td>
                        <td>${order.quantity}</td>
                        <td>${order.status}</td>
                        <td>
                            <button onclick="deleteOrder(${order.orderId})">Delete</button>
                        </td>
                    </tr>
                `;
                tableBody.innerHTML += row;
            });
        })
        .catch(error => console.error("Error loading orders:", error));
}

function saveOrder(event) {
    event.preventDefault();

    const order = {
        orderName: document.getElementById("orderName").value,
        productName: document.getElementById("productName").value,
        quantity: parseInt(document.getElementById("quantity").value),
        status: document.getElementById("status").value
    };

    fetch(API_URL, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(order)
    })
    .then(() => {
        document.getElementById("orderForm").reset();
        loadOrders();
    })
    .catch(error => console.error("Error saving order:", error));
}

function deleteOrder(id) {
    if (confirm("Are you sure you want to delete this order?")) {
        fetch(`${API_URL}/${id}`, {
            method: "DELETE"
        })
        .then(() => loadOrders())
        .catch(error => console.error("Error deleting order:", error));
    }
}