# -*- coding: utf-8 -*-
import re, os
base = r'c:\Users\HP\DT_textile_industry\src\main\resources'
for fn in ['messages.properties', 'messages_ta.properties']:
    keys = []
    for line in open(os.path.join(base, fn), encoding='utf-8-sig'):
        line = line.strip()
        if line and not line.startswith('#') and '=' in line:
            keys.append(line.split('=', 1)[0])
    print(fn, len(keys))
    open(r'c:\Users\HP\DT_textile_industry\keys_' + fn + '.txt', 'w', encoding='utf-8').write('\n'.join(keys))
print('same-set:', open(r'c:\Users\HP\DT_textile_industry\keys_messages.properties.txt', encoding='utf-8').read().split() == open(r'c:\Users\HP\DT_textile_industry\keys_messages_ta.properties.txt', encoding='utf-8').read().split())
