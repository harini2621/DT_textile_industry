const fs = require('fs');
const path = require('path');
const TPL = path.join(__dirname, '..', 'src', 'main', 'resources', 'templates');
const ALLOW = new Set(['TRIZEN','PDF','Excel','raquo','copy']);
let issues = 0;
for (const f of fs.readdirSync(TPL).sort()){
  let s = fs.readFileSync(path.join(TPL, f), 'utf8');
  // protect > and < inside thymeleaf expressions / JS template bits
  s = s.replace(/([$#]\{[^}]*\}|\[\[[\s\S]*?\]\])/g, m => m.replace(/>/g, '__GT__').replace(/</g, '__LT__'));
  s = s.replace(/<!--[\s\S]*?-->/g, '');
  s = s.replace(/<script[\s\S]*?<\/script>/gi, '');
  s = s.replace(/<style[\s\S]*?<\/style>/gi, '');
  // remove whole elements whose open tag carries a th text-bearing attribute
  let prev;
  do {
    prev = s;
    s = s.replace(/<([a-zA-Z][a-zA-Z0-9]*)[^>]*\sth:(?:text|utext|placeholder|title|alt|label|value|header|summary)\s*=[^>]*>[\s\S]*?<\/\1\s*>/g, '');
  } while (s !== prev);
  s = s.replace(/<[^>]+>/g, ' ');
  s = s.replace(/\$\{[^}]*\}/g, ' ');
  s = s.replace(/#\{[^}]*\}/g, ' ');
  const m = s.match(/[A-Za-z][A-Za-z'’!?,.()\/:%-]*/g) || [];
  const bad = [...new Set(m.map(x => x.trim()).filter(x =>
    x.length > 2 && !ALLOW.has(x) && /[a-z]/.test(x)
  ))];
  if (bad.length){ issues++; console.log(f + ' -> ' + JSON.stringify(bad)); }
}
console.log(issues === 0 ? 'CLEAN: no hardcoded text found' : issues + ' file(s) with suspects (review above)');

