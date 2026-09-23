const {load,save} = require('./lib');

const FILES = ['admin-activity-logs','admin-dashboard','admin-notifications','admin-users',
  'create-order','owner-add-worker','owner-assign-worker','owner-dashboard','owner-orders',
  'owner-payment-tracking','owner-production-batch','owner-production-tracking','owner-search',
  'production-orders','profile','reports','tasks','worker-add-stock','worker-dashboard',
  'worker-my-tasks','worker-notifications','worker-orders','worker-payment-history',
  'worker-stock','worker-work-history','workers'];

// ---- 1. Remove sidebar language links (globe icon list items) ----
const sidebarRe = /^[ \t]*<li><a href="\?lang=(?:en|ta)"><i class="bi bi-globe2"><\/i>[^\n]*\n/gm;
for (const f of FILES){
  let t = load(f + '.html');
  const before = (t.match(sidebarRe) || []).length;
  t = t.replace(sidebarRe, '');
  // also catch any stray sidebar-style lang <li> without globe icon
  const strayRe = /^[ \t]*<li><a href="\?lang=(?:en|ta)"><\/a><\/li>\n/gm;
  t = t.replace(strayRe, '');
  save(f + '.html', t);
  console.log('sidebar removed ' + f + ': ' + before);
}

// ---- 2. Insert login-style selector into .topbar (last child => top-right) ----
function buildSel(ind){
  const c = ind + '    ';
  return c + '<div class="dropdown">\n'
    + c + '    <button class="btn btn-sm btn-outline-secondary dropdown-toggle rounded-pill fw-semibold" type="button" data-bs-toggle="dropdown">\n'
    + c + '        <i class="bi bi-translate me-1"></i><span th:text="${#locale.language == \'ta\'} ? #{lang.ta} : #{lang.en}">English</span>\n'
    + c + '    </button>\n'
    + c + '    <ul class="dropdown-menu dropdown-menu-end">\n'
    + c + '        <li><a class="dropdown-item" href="?lang=en" th:text="#{lang.en}">English</a></li>\n'
    + c + '        <li><a class="dropdown-item" href="?lang=ta" th:text="#{lang.ta}">\u0ba4\u0bae\u0bbf\u0bb4\u0bcd</a></li>\n'
    + c + '    </ul>\n'
    + c + '</div>\n';
}
for (const f of FILES){
  let t = load(f + '.html');
  const start = t.indexOf('<div class="topbar">');
  if (start === -1){ console.log('NO TOPBAR ' + f); continue; }
  const ind = t.slice(t.lastIndexOf('\n', start) + 1, start);
  // find matching closing </div> of the topbar
  const re = /<div\b|<\/div>/g;
  re.lastIndex = start;
  let depth = 0, m, end = -1;
  while ((m = re.exec(t))){
    if (m[0] === '</div>'){ depth--; if (depth === 0){ end = m.index; break; } }
    else depth++;
  }
  if (end === -1){ console.log('UNBALANCED ' + f); continue; }
  // already has a dropdown inside the topbar? skip
  if (t.slice(start, end).indexOf('data-bs-toggle="dropdown"') !== -1){ console.log('already has selector ' + f); continue; }
  const lineStart = t.lastIndexOf('\n', start) === -1 ? 0 : t.lastIndexOf('\n', end) + 1;
  const beforeClose = t.slice(lineStart, end);
  const sel = buildSel(ind);
  if (/^[ \t]*$/.test(beforeClose)){
    t = t.slice(0, lineStart) + sel + t.slice(lineStart);
  } else {
    t = t.slice(0, end) + '\n' + sel + ind + t.slice(end);
  }
  save(f + '.html', t);
  console.log('topbar selector added ' + f);
}
console.log('done');
