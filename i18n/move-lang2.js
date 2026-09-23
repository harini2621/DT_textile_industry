const {load,save} = require('./lib');

// ---- error.html: move selector to top-right corner of the page ----
{
  const F = 'error.html';
  let t = load(F);
  const oldOpen = '<div class="mb-4 d-flex justify-content-center">';
  const newOpen = '<div class="position-absolute top-0 end-0 p-3">';
  if (t.indexOf(oldOpen) !== -1){
    t = t.split(oldOpen).join(newOpen);
    save(F, t);
    console.log(F + ': selector moved to top-right');
  } else {
    console.log(F + ': MISS wrapper');
  }
}

// ---- index.html: use the exact login-page selector UI in the navbar (rightmost) ----
{
  const F = 'index.html';
  let t = load(F);
  const oldAnchor = '<a class="nav-link dropdown-toggle" href="#" role="button" data-bs-toggle="dropdown" aria-expanded="false">\n                        <i class="bi bi-translate"></i>\n                        <span th:text="${#locale.language == \'ta\'} ? #{lang.ta} : #{lang.en}">English</span>\n                    </a>';
  const newBtn = '<button class="btn btn-sm btn-outline-secondary dropdown-toggle rounded-pill fw-semibold" type="button" data-bs-toggle="dropdown">\n                        <i class="bi bi-translate me-1"></i>\n                        <span th:text="${#locale.language == \'ta\'} ? #{lang.ta} : #{lang.en}">English</span>\n                    </button>';
  if (t.indexOf(oldAnchor) !== -1){
    t = t.split(oldAnchor).join(newBtn);
    save(F, t);
    console.log(F + ': selector replaced with login-style UI');
  } else {
    console.log(F + ': MISS anchor');
  }
}
