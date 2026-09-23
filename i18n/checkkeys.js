const fs = require('fs');
const path = require('path');
const TPL = path.join(__dirname, '..', 'src', 'main', 'resources', 'templates');

function keysOf(f){
  const s = fs.readFileSync(f, 'utf8');
  const set = new Set();
  for (const m of s.matchAll(/#\{([^}]+)\}/g)) set.add(m[1].trim().split(',')[0].replace(/\(.*$/, ''));
  for (const m of s.matchAll(/msg(?:OrNull)?\('([^']+)'\)/g)) set.add(m[1]);
  return set;
}
function bundle(f){
  const set = new Set();
  for (const line of fs.readFileSync(f, 'utf8').split(/\r?\n/)){
    const t = line.trim();
    if (!t || t.startsWith('#')) continue;
    const i = t.indexOf('=');
    if (i > 0) set.add(t.slice(0, i));
  }
  return set;
}
const en = bundle(path.join(__dirname, '..', 'src', 'main', 'resources', 'messages.properties'));
const ta = bundle(path.join(__dirname, '..', 'src', 'main', 'resources', 'messages_ta.properties'));
let all = new Set();
const perFile = {};
for (const f of fs.readdirSync(TPL)){
  const k = keysOf(path.join(TPL, f));
  perFile[f] = k;
  k.forEach(x => all.add(x));
}
const missEn = [...all].filter(k => !en.has(k));
const missTa = [...all].filter(k => !ta.has(k));
console.log('templates:', Object.keys(perFile).length, 'keys referenced:', all.size, 'en:', en.size, 'ta:', ta.size);
console.log('MISSING in en:', missEn);
console.log('MISSING in ta:', missTa);
for (const f of Object.keys(perFile)){
  const m = [...perFile[f]].filter(k => !en.has(k));
  if (m.length) console.log(f, '->', m);
}
const enOnly = [...en].filter(k => !ta.has(k));
const taOnly = [...ta].filter(k => !en.has(k));
console.log('en-but-not-ta:', enOnly);
console.log('ta-but-not-en:', taOnly);
