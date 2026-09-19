/*=========================================================
 TRIZEN ERP MANAGEMENT SYSTEM
 Ultra Premium Enterprise JavaScript
=========================================================*/

"use strict";

/* ===========================
   DOM Ready
=========================== */

document.addEventListener("DOMContentLoaded", () => {

    sidebarToggle();
    activeMenu();
    animateCounters();
    animateProgressBars();
    cardHoverEffects();

});

/* ===========================
   Sidebar Toggle
=========================== */

function sidebarToggle() {

    const sidebar = document.querySelector(".sidebar");
    const toggle = document.querySelector(".menu-toggle");

    if (!sidebar || !toggle) return;

    toggle.addEventListener("click", () => {

        sidebar.classList.toggle("collapsed");

    });

}

/* ===========================
   Active Navigation
=========================== */

function activeMenu() {

    const current = window.location.pathname;

    document.querySelectorAll(".sidebar a").forEach(link => {

        link.classList.remove("active");

        if (link.getAttribute("href") === current) {

            link.classList.add("active");

        }

    });

}

/* ===========================
   Dashboard Counter Animation
=========================== */

function animateCounters() {

    const counters = document.querySelectorAll(".dashboard-card h3");

    counters.forEach(counter => {

        const target = parseInt(counter.textContent) || 0;

        let value = 0;

        const speed = Math.max(15, Math.floor(1500 / (target || 1)));

        counter.textContent = "0";

        const timer = setInterval(() => {

            value++;

            counter.textContent = value;

            if (value >= target) {

                counter.textContent = target;

                clearInterval(timer);

            }

        }, speed);

    });

}

/* ===========================
   Progress Bar Animation
=========================== */

function animateProgressBars() {

    const bars = document.querySelectorAll(".progress-bar");

    bars.forEach(bar => {

        const width = bar.style.width;

        bar.style.width = "0%";

        setTimeout(() => {

            bar.style.width = width;

        }, 300);

    });

}

/* ===========================
   Card Hover Animation
=========================== */

function cardHoverEffects() {

    document.querySelectorAll(".dashboard-card,.glass-card")
        .forEach(card => {

            card.addEventListener("mouseenter", () => {

                card.style.transform = "translateY(-8px)";

            });

            card.addEventListener("mouseleave", () => {

                card.style.transform = "translateY(0)";

            });

        });

}

/* ===========================
   Table Search Filter
=========================== */

function tableSearch(inputSelector, tableSelector) {

    const input = document.querySelector(inputSelector);
    const table = document.querySelector(tableSelector);

    if (!input || !table) return;

    input.addEventListener("keyup", () => {

        const value = input.value.toLowerCase();

        table.querySelectorAll("tbody tr").forEach(row => {

            row.style.display =
                row.innerText.toLowerCase().includes(value)
                    ? ""
                    : "none";

        });

    });

}

tableSearch(".table-search", ".table");

/* ===========================
   Form Validation
=========================== */

document.querySelectorAll("form").forEach(form => {

    form.addEventListener("submit", function (e) {

        let valid = true;

        this.querySelectorAll("[required]").forEach(field => {

            if (field.value.trim() === "") {

                field.classList.add("is-invalid");

                valid = false;

            } else {

                field.classList.remove("is-invalid");

                field.classList.add("is-valid");

            }

        });

        if (!valid) {

            e.preventDefault();

            showToast("Please fill all required fields.", "danger");

        }

    });

});

/* ===========================
   Toast Notification
=========================== */

function showToast(message, type = "success") {

    const toast = document.createElement("div");

    toast.className = `alert alert-${type}`;

    toast.style.position = "fixed";
    toast.style.top = "20px";
    toast.style.right = "20px";
    toast.style.zIndex = "9999";
    toast.style.minWidth = "300px";
    toast.style.borderRadius = "12px";
    toast.style.boxShadow = "0 10px 25px rgba(0,0,0,.15)";

    toast.innerHTML = message;

    document.body.appendChild(toast);

    setTimeout(() => {

        toast.style.opacity = "0";
        toast.style.transition = ".5s";

        setTimeout(() => toast.remove(), 500);

    }, 3000);

}

/* ===========================
   Loading Spinner
=========================== */

function showLoader() {

    const loader = document.querySelector(".loader");

    if (loader)
        loader.style.display = "flex";

}

function hideLoader() {

    const loader = document.querySelector(".loader");

    if (loader)
        loader.style.display = "none";

}

window.addEventListener("load", hideLoader);

/* ===========================
   Dark Mode Toggle
=========================== */

function darkModeToggle() {

    const toggle = document.querySelector(".theme-toggle");

    if (!toggle) return;

    const savedTheme = localStorage.getItem("theme");

    if (savedTheme === "dark") {
        document.body.classList.add("dark-mode");
    }

    toggle.addEventListener("click", () => {

        document.body.classList.toggle("dark-mode");

        const theme = document.body.classList.contains("dark-mode")
            ? "dark"
            : "light";

        localStorage.setItem("theme", theme);

    });

}

darkModeToggle();

/* ===========================
   Smooth Scroll
=========================== */

document.querySelectorAll('a[href^="#"]').forEach(link => {

    link.addEventListener("click", function (e) {

        e.preventDefault();

        const target = document.querySelector(this.getAttribute("href"));

        if (target) {

            target.scrollIntoView({

                behavior: "smooth",
                block: "start"

            });

        }

    });

});

/* ===========================
   Back To Top Button
=========================== */

const backTop = document.createElement("button");

backTop.innerHTML = '<i class="bi bi-arrow-up"></i>';

backTop.className = "btn btn-primary";

backTop.style.position = "fixed";
backTop.style.bottom = "25px";
backTop.style.right = "25px";
backTop.style.borderRadius = "50%";
backTop.style.width = "55px";
backTop.style.height = "55px";
backTop.style.display = "none";
backTop.style.zIndex = "999";

document.body.appendChild(backTop);

window.addEventListener("scroll", () => {

    if (window.scrollY > 300) {

        backTop.style.display = "block";

    } else {

        backTop.style.display = "none";

    }

});

backTop.addEventListener("click", () => {

    window.scrollTo({

        top: 0,
        behavior: "smooth"

    });

});

/* ===========================
   Fade In Animation
=========================== */

const observer = new IntersectionObserver(entries => {

    entries.forEach(entry => {

        if (entry.isIntersecting) {

            entry.target.style.opacity = "1";
            entry.target.style.transform = "translateY(0)";

        }

    });

}, {
    threshold: 0.15
});

document.querySelectorAll(".glass-card, .dashboard-card").forEach(card => {

    card.style.opacity = "0";
    card.style.transform = "translateY(30px)";
    card.style.transition = "all .6s ease";

    observer.observe(card);

});

/* ===========================
   Auto Hide Alerts
=========================== */

document.querySelectorAll(".alert").forEach(alert => {

    setTimeout(() => {

        alert.style.transition = ".5s";
        alert.style.opacity = "0";

        setTimeout(() => alert.remove(), 500);

    }, 4000);

});

/* ===========================
   Console Message
=========================== */

console.log("%cTRIZEN ERP Loaded Successfully",
    "color:#2563eb;font-size:18px;font-weight:bold;");

/* ===========================
   End of File
=========================== */