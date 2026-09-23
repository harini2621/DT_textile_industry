# -*- coding: utf-8 -*-
import re, os
TPL = r'c:\Users\HP\DT_textile_industry\src\main\resources\templates'
files = ['admin-dashboard.html','create-order.html','owner-add-worker.html','owner-assign-worker.html','owner-dashboard.html','owner-orders.html','owner-payment-tracking.html','owner-production-batch.html','owner-production-tracking.html','owner-search.html','worker-add-stock.html','worker-dashboard.html','worker-my-tasks.html','worker-notifications.html','worker-orders.html','worker-payment-history.html','worker-stock.html','worker-work-history.html']
for fn in files:
    t = open(os.path.join(TPL, fn), encoding='utf-8-sig').read()
    t2 = re.sub(r'<script.*?</script>', '', t, flags=re.DOTALL|re.IGNORECASE)
    t2 = re.sub(r'<!--.*?-->', '', t2, flags=re.DOTALL)
    nodes = re.findall(r'>((?:[^<>$#{}])*?)<', t2)
    seen = []
    for n in nodes:
        s = re.sub(r'\s+', ' ', n).strip()
        if len(s) >= 2 and re.search(r'[A-Za-z]{2,}', s) and s not in seen:
            seen.append(s)
    print('===== ' + fn + ' (' + str(len(seen)) + ') =====')
    for s in seen:
        print('  |' + s)
