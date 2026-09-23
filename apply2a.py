# -*- coding: utf-8 -*-
"""Phase-2 bulk i18n applier. Idempotent; safe re-run."""
import os, re
BASE = r'c:\Users\HP\DT_textile_industry\src\main\resources'
TPL = os.path.join(BASE, 'templates')
ENP = os.path.join(BASE, 'messages.properties')
TAP = os.path.join(BASE, 'messages_ta.properties')
LOG = []
def load(p):
    with open(p, encoding='utf-8-sig') as f:
        return f.read()
def save(p, t):
    with open(p, 'w', encoding='utf-8') as f:
        f.write(t)
def R(fname, old, new, expect=0):
    p = os.path.join(TPL, fname)
    t = load(p)
    n = t.count(old)
    if n == 0:
        LOG.append('MISS %s :: %r' % (fname, old[:80])); return
    if expect and n != expect:
        LOG.append('COUNT %s got=%d exp=%d :: %r' % (fname, n, expect, old[:70]))
    t = t.replace(old, new)
    save(p, t)
    LOG.append('OK %s x%d :: %r' % (fname, n, old[:70]))
