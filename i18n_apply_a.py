# -*- coding: utf-8 -*-
"""Apply replacements A: helpers + admin-dashboard + owner dashboards."""
import os
BASE = r'c:\Users\HP\DT_textile_industry\src\main\resources'
TPL = os.path.join(BASE, 'templates')
LOG = []
def load(p):
    with open(p, encoding='utf-8-sig') as f: return f.read()
def save(p, t):
    with open(p, 'w', encoding='utf-8') as f: f.write(t)
def rep(fname, old, new, count=0):
    p = os.path.join(TPL, fname)
    t = load(p); n = t.count(old)
    if n == 0:
        LOG.append(f'{fname}: MISS :: {old[:70]!r}'); return
    if count and n != count:
        LOG.append(f'{fname}: COUNT {n} (exp {count}) :: {old[:60]!r}')
    t = t.replace(old, new); save(p, t)
    LOG.append(f'{fname}: OK x{n} :: {old[:60]!r}')
def html_tag(fname):
    rep(fname, '<html lang=\"en\" xmlns:th=', '<html lang=\"en\" th:lang=\"${#locale.language}\" xmlns:th=')
SWITCHER = ('<main class=\"main-content\">',
 '<main class=\"main-content\">\n        <div class=\"d-flex justify-content-end mb-2\">\n'
 '            <div class=\"dropdown\">\n'
 '                <button class=\"btn btn-sm btn-outline-secondary dropdown-toggle rounded-pill fw-semibold\" type=\"button\" data-bs-toggle=\"dropdown\">\n'
 '                    <i class=\"bi bi-translate me-1\"></i><span th:text=\"${#locale.language == \\'ta\\'} ? #{lang.ta} : #{lang.en}\">English</span>\n'
 '                </button>\n'
 '                <ul class=\"dropdown-menu dropdown-menu-end\">\n'
 '                    <li><a class=\"dropdown-item\" href=\"?lang=en\" th:text=\"#{lang.en}\">English</a></li>\n'
 '                    <li><a class=\"dropdown-item\" href=\"?lang=ta\" th:text=\"#{lang.ta}\">\\u0ba4\\u0bae\\u0bbf\\u0bb4\\u0bcd</a></li>\n'
 '                </ul>\n            </div>\n        </div>')
def add_switcher(fname):
    p = os.path.join(TPL, fname); t = load(p)
    if 'bi-translate' in t:
        LOG.append(f'{fname}: switcher already present'); return
    if '<main class=\"main-content\">' not in t:
        LOG.append(f'{fname}: NO main-content for switcher'); return
    t = t.replace(SWITCHER[0], SWITCHER[1], 1); save(p, t)
    LOG.append(f'{fname}: switcher injected')
def nav_common(fname):
    for eng, key in [('Dashboard','nav.dashboard'),('Search Stock','nav.search.stock'),
        ('Orders','nav.orders'),('Workers','nav.workers'),('Reports','nav.reports'),
        ('Production','nav.production'),('Payments','nav.payments'),('Stock','nav.stock'),
        ('Add Stock','nav.add.stock'),('Create Order','nav.create.order'),
        ('My Tasks','nav.my.tasks'),('Work History','nav.work.history'),
        ('Payment History','nav.payment.history'),('Tasks','nav.tasks'),
        ('Profile','nav.profile'),('Notifications','nav.notifications')]:
        for pat in [f'</i> {eng}</a>', f'</i>{eng}</a>', f'</i> {eng}\\n', f'>{eng}</a>']:
            p = os.path.join(TPL, fname); t = load(p)
            if pat in t:
                t = t.replace(pat, pat.replace(eng, f'<span th:text=\"#{{{key}}}\">{eng}</span>'))
                save(p, t); LOG.append(f'{fname}: nav {eng}'); break
    rep(fname, '</i> Logout</button>', '</i> <span th:text=\"#{nav.logout}\">Logout</span></button>')
    rep(fname, '</i> Logout', '</i> <span th:text=\"#{nav.logout}\">Logout</span>')
