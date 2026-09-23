const {load,save} = require('./lib');
function add(f, k, v){
  let t = load(f);
  if (t.split('\n').some(l => l.trim().startsWith(k + '='))){ console.log('skip', k); return; }
  save(f, t.trimEnd() + '\n' + k + '=' + v + '\n');
  console.log('added', k, 'to', f);
}
add('../messages.properties', 'orders.search.ph', 'Search order...');
add('../messages_ta.properties', 'orders.search.ph', '\u0b86\u0bae\u0bc7\u0bb4\u0d4d\u0b95\u0bc1\u0bb0\u0bc1\u0b95\u0bb3\u0bc8 \u0ba4\u0bc7\u0b9f\u0bc1...');
