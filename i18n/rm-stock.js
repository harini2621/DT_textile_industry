const fs = require('fs');
const path = require('path');
const T = path.join(__dirname, '..', 'src', 'main', 'resources', 'templates');
const read  = f => fs.readFileSync(path.join(T, f), 'utf8');
const write = (f, c) => fs.writeFileSync(path.join(T, f), c, 'utf8');

// 1) Remove the sidebar Stock <li> from all worker-side templates (exactly once each).
const SIDELI = '<li><a href="/worker-stock"><i class="bi bi-box-seam"></i> <span th:text="#{nav.stock}">Stock</span></a></li>';
const sideFiles = ['production-orders.html', 'tasks.html', 'worker-dashboard.html',
    'worker-my-tasks.html', 'worker-notifications.html', 'worker-orders.html',
    'worker-payment-history.html', 'worker-work-history.html'];
for (const f of sideFiles) {
    const c = read(f);
    const eol = c.includes('\r\n') ? '\r\n' : '\n';
    const lines = c.split(/\r?\n/);
    const keep = lines.filter(l => l.trim() !== SIDELI);
    const removed = lines.length - keep.length;
    if (removed !== 1) throw new Error(`${f}: expected 1 sidebar stock link, found ${removed}`);
    write(f, keep.join(eol));
    console.log('sidebar stock link removed:', f);
}

// 2) worker-dashboard.html: remove Add Stock button, stock stat card,
//    stock progress block and Update Stock quick-action card; rebalance rows.
let c = read('worker-dashboard.html');
const eol = c.includes('\r\n') ? '\r\n' : '\n';
const J = (...ls) => ls.join(eol);
function rep(oldLines, newLines, label, expect = 1) {
    const o = J(...oldLines);
    const n = newLines ? J(...newLines) : '';
    const parts = c.split(o);
    if (parts.length - 1 !== expect) throw new Error(`${label}: expected ${expect}, found ${parts.length - 1}`);
    c = parts.join(n);
    console.log('ok:', label);
}

// (a) topbar "Add Stock" button (trailing blank line removed too)
rep(['            <a href="/worker-add-stock" class="btn btn-primary rounded-pill">',
     '                <i class="bi bi-plus-circle-fill"></i> <span th:text="#{worker.dash.add.stock}">Add Stock</span>',
     '            </a>', ''], null, 'topbar Add Stock');

// (b) "Stock Updated" stats card (last of the four stat cards)
rep(['            <div class="col-lg-3 col-md-6">',
     '                <div class="dashboard-card">',
     '                    <i class="bi bi-box-seam-fill icon purple"></i>',
     '                    <h3 th:text="${myStockCount}">0</h3>',
     '                    <p th:text="#{stat.stock.updated}">Stock Updated</p>',
     '                </div>',
     '            </div>', ''], null, 'stock stats card');

// (c) "Stock Updated (units)" block inside Today's Progress
rep(['                    <div>',
     '                        <small class="text-muted fw-semibold" th:text="#{worker.progress.stock.updated}">Stock Updated (units)</small>',
     '                        <h4 class="text-primary fw-bold" th:text="${myStockCount}">0</h4>',
     '                    </div>', ''], null, 'progress stock block');

// (d) "Update Stock" quick-action card (first of three)
rep(['            <div class="col-lg-4">',
     '                <div class="glass-card text-center p-4">',
     '                    <i class="bi bi-box-seam display-4 text-primary"></i>',
     '                    <h5 class="mt-3" th:text="#{worker.dash.update.stock}">Update Stock</h5>',
     '                    <p class="text-muted mb-3" th:text="#{worker.quick.update.stock.desc}">Record newly produced fabrics and update inventory.</p>',
     '                    <a href="/worker-stock" class="btn btn-outline-primary rounded-pill" th:text="#{common.update}">Update</a>',
     '                </div>',
     '            </div>', ''], null, 'quick action stock card');

// (e) rebalance the 3 remaining stat cards across the full row
rep(['<div class="col-lg-3 col-md-6">'], ['<div class="col-lg-4 col-md-4">'], 'rebalance stat cards', 3);

// (f) rebalance the 2 remaining quick-action cards
rep(['            <div class="col-lg-4">',
     '                <div class="glass-card text-center p-4">'],
    ['            <div class="col-lg-6">',
     '                <div class="glass-card text-center p-4">'], 'rebalance quick actions', 2);

write('worker-dashboard.html', c);

// 3) Delete the worker Stock templates (no controller serves them any more).
for (const f of ['worker-stock.html', 'worker-add-stock.html']) {
    const p = path.join(T, f);
    if (!fs.existsSync(p)) throw new Error(f + ' missing');
    fs.unlinkSync(p);
    console.log('deleted:', f);
}
console.log('DONE');