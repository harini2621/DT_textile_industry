const {load,save} = require('./lib');
const EN = '../messages.properties';
const rows = [
['owner.stat.completed.orders','Completed Orders'],
['owner.live','Live'],
['owner.track.batches','Production Batches'],
['table.col.batch.no','Batch #'],
['common.save','Save'],
['worker.tasks.list','Task List'],
['prod.current.prefix','Current: {0}'],
['order.no.prefix','Order # {0}'],
['owner.search.no.stock.for','No stock found for "{0}"'],
['stage.cutting','Cutting'],
['stage.stitching','Stitching'],
['stage.dyeing','Dyeing'],
['stage.finishing','Finishing'],
['stage.packing','Packing'],
['stage.completed','Completed'],
['stage.not.started','Not started'],
['pay.amount.ph','Amount (\u20b9)'],
['pay.remarks.ph','Remarks'],
['owner.track.update.stage','Update stage'],
['owner.track.assign.worker','Assign worker'],
['img.alt.textile.industry','Textile Industry'],
['img.alt.textile','Textile']
];
let t = load(EN);
let n = 0;
const lines = t.split('\n');
for (const [k, v] of rows){
  if (lines.some(l => l.trim().startsWith(k + '='))) { console.log('skip', k); continue; }
  t = t.trimEnd() + '\n' + k + '=' + v + '\n';
  n++;
}
save(EN, t);
console.log('en appended', n);
