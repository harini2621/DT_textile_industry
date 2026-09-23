const {load,save} = require('./lib');
const TA = '../messages_ta.properties';
const rows = [
['owner.stat.completed.orders','\u0bae\u0bc1\u0b9f\u0bbf\u0bb5\u0bc1\u0b95\u0bcd\u0b95\u0baa\u0d4d\u0baa \u0b86\u0bae\u0bc7\u0bb4\u0bcd\u0b95\u0bb3\u0bcd'],
['owner.live','\u0ba8\u0bc7\u0bb0\u0bcd'],
['owner.track.batches','\u0ba4\u0baf\u0bbe\u0bb0\u0bc1 \u0ba4\u0bca\u0ba9\u0bcd\u0ba4\u0bc1\u0b95\u0bb3\u0d4d'],
['table.col.batch.no','\u0ba4\u0bca\u0ba9\u0bcd\u0ba4\u0bc1 #'],
['common.save','\u0bb8\u0bc7\u0bae\u0bbf'],
['worker.tasks.list','\u0baa\u0ba3\u0bbf \u0baa\u0b9f\u0bcd\u0b9f\u0bbf\u0baf\u0bb2\u0d4d'],
['prod.current.prefix','\u0ba4\u0bb1\u0bcd\u0baa\u0bcb\u0ba4\u0baf\u0bcd: {0}'],
['order.no.prefix','\u0b86\u0bae\u0bc7\u0bb4\u0d4d # {0}'],
['owner.search.no.stock.for','{0} \u0b89\u0ba4\u0bb0\u0bc1\u0b95\u0d4d\u0b95\u0bc1 \u0b87\u0bb0\u0bc1\u0baa\u0bcd\u0baa\u0bc1 \u0b87\u0bb2\u0bcd\u0bb2\u0bc8'],
['stage.cutting','\u0bb5\u0bc6\u0b9f\u0bcd\u0b9f\u0bc1\u0ba4\u0bb2\u0d4d'],
['stage.stitching','\u0ba4\u0bc8\u0baf\u0bb2\u0d4d'],
['stage.dyeing','\u0b9a\u0bbe\u0baf\u0bae\u0bbf\u0b9f\u0bc1\u0ba4\u0bb2\u0d4d'],
['stage.finishing','\u0bae\u0bc1\u0b9f\u0bbf\u0ba4\u0bcd\u0ba4\u0bb2\u0d4d'],
['stage.packing','\u0baa\u0bc7\u0b95\u0bcd\u0b95\u0bbf\u0b99\u0bcd'],
['stage.completed','\u0bae\u0bc1\u0b9f\u0bbf\u0bb5\u0bc1\u0b95\u0bcd\u0b95\u0baa\u0d4d\u0baa\u0ba4\u0bc1'],
['stage.not.started','\u0ba4\u0bc1\u0b9f\u0b99\u0bcd\u0b95\u0bb5\u0bbf\u0bb2\u0bcd\u0bb2\u0bc8'],
['pay.amount.ph','\u0ba4\u0bc0\u0b9f\u0bb2\u0d4d (\u20b9)'],
['pay.remarks.ph','\u0b95\u0bc1\u0bb1\u0bbf\u0baa\u0bcd\u0baa\u0b95\u0bb3\u0d4d'],
['owner.track.update.stage','\u0ba8\u0bbf\u0bb2\u0bc8\u0baf\u0bc8 \u0baa\u0bc1\u0ba4\u0bc1\u0baa\u0ccd\u0baa\u0bbf'],
['owner.track.assign.worker','\u0baa\u0ba3\u0bbf\u0baf\u0bbe\u0b95\u0bc0\u0b95\u0bb3\u0bb5\u0bb0\u0bc8 \u0b92\u0ba4\u0bc1\u0b95\u0d4d\u0b95\u0bc1'],
['img.alt.textile.industry','\u0b9c\u0bb5\u0bc1\u0bb3\u0bbf\u0ba4\u0bcd \u0ba4\u0b9a\u0bc1\u0bb5\u0bbf\u0bb0\u0bcd\u0b9a\u0bbe\u0bb2\u0bc8'],
['img.alt.textile','\u0b9c\u0bb5\u0bc1\u0bb3\u0bbf']
];
let t = load(TA);
let n = 0;
const lines = t.split('\n');
for (const [k, v] of rows){
  if (lines.some(l => l.trim().startsWith(k + '='))) { console.log('skip', k); continue; }
  t = t.trimEnd() + '\n' + k + '=' + v + '\n';
  n++;
}
save(TA, t);
console.log('ta appended', n);
