const fs = require('fs');
const TPL = 'src/main/resources/templates';
function load(f){ return fs.readFileSync(TPL+'/'+f,'utf8').replace(/^\uFEFF/,''); }
function save(f,t){ fs.writeFileSync(TPL+'/'+f,t,'utf8'); }
function R(f,oldText,newText){
  let t = load(f);
  const n = t.split(oldText).length-1;
  if(n===0){ console.log('MISS '+f+' :: '+JSON.stringify(oldText.slice(0,70))); return; }
  t = t.split(oldText).join(newText);
  save(f,t);
  console.log('OK '+f+' x'+n+' :: '+JSON.stringify(oldText.slice(0,60)));
}
module.exports = {load,save,R};
