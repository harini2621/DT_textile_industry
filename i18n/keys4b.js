const fs=require('fs');
const ENP='src/main/resources/messages.properties';
const TAP='src/main/resources/messages_ta.properties';
const ADDED=[
['worker.orders.assigned','Assigned Orders','\u0b92\u0ba4\u0bc1\u0b95\u0bcd\u0b95\u0baa\u0bcd\u0baa\u0b9f\u0bcd\u0b9f \u0b86\u0ba3\u0bc8\u0b95\u0bb3\u0bcd'],
['worker.orders.empty','No orders assigned to you yet.','\u0b89\u0b99\u0bcd\u0b95\u0bb3\u0bc1\u0b95\u0bcd\u0b95\u0bc1 \u0b86\u0ba3\u0bc8\u0b95\u0bb3\u0bcd \u0b87\u0bb2\u0bcd\u0bb2\u0bc8.'],
['worker.pay.page.title','Payment History','\u0b95\u0bca\u0b9f\u0bc1\u0baa\u0bcd\u0baa\u0ba9\u0bb5\u0bc1 \u0bb5\u0bb0\u0bb2\u0bbe\u0bb1\u0bc1'],
['worker.pay.sub','Payments recorded for your completed work.','\u0bae\u0bc1\u0b9f\u0bbf\u0ba4\u0bcd\u0ba4 \u0bb5\u0bc7\u0bb2\u0bc8\u0b95\u0bcd\u0b95\u0bbe\u0ba9 \u0b95\u0bca\u0b9f\u0bc1\u0baa\u0bcd\u0baa\u0ba9\u0bb5\u0bc1\u0b95\u0bb3\u0bcd.'],
['worker.pay.my','My Payments','\u0b8e\u0ba9\u0bcd \u0b95\u0bca\u0b9f\u0bc1\u0baa\u0bcd\u0baa\u0ba9\u0bb5\u0bc1\u0b95\u0bb3\u0bcd'],
['worker.pay.empty','No payments recorded yet.','\u0b95\u0bca\u0b9f\u0bc1\u0baa\u0bcd\u0baa\u0ba9\u0bb5\u0bc1\u0b95\u0bb3\u0bcd \u0b87\u0bb2\u0bcd\u0bb2\u0bc8.'],
['worker.stock.page.title','Stock Inventory','\u0b9a\u0bb0\u0b95\u0bcd\u0b95\u0bc1 \u0b87\u0bb0\u0bc1\u0baa\u0bcd\u0baa\u0bc1'],
['worker.stock.sub','View and manage your stock entries.','\u0b9a\u0bb0\u0b95\u0bcd\u0b95\u0bc1 \u0baa\u0ba4\u0bbf\u0bb5\u0bc1\u0b95\u0bb3\u0bc8 \u0b95\u0ba3\u0bcd\u0b9f\u0bc1 \u0ba8\u0bbf\u0bb0\u0bcd\u0bb5\u0b95\u0bbf\u0b95\u0bcd\u0b95\u0bb5\u0bc1\u0bae\u0bcd.'],
['worker.stock.my','My Stock Entries','\u0b8e\u0ba9\u0bcd \u0b9a\u0bb0\u0b95\u0bcd\u0b95\u0bc1 \u0baa\u0ba4\u0bbf\u0bb5\u0bc1\u0b95\u0bb3\u0bcd'],
['worker.stock.empty','No stock entries yet.','\u0b9a\u0bb0\u0b95\u0bcd\u0b95\u0bc1 \u0baa\u0ba4\u0bbf\u0bb5\u0bc1\u0b95\u0bb3\u0bcd \u0b87\u0bb2\u0bcd\u0bb2\u0bc8.'],
['worker.add.page.title','Add New Stock','\u0baa\u0bc1\u0ba4\u0bbf\u0baf \u0b9a\u0bb0\u0b95\u0bcd\u0b95\u0bc1 \u0b9a\u0bc7\u0bb0\u0bcd'],
['worker.add.sub','Add raw materials to inventory.','\u0bae\u0bc2\u0bb2\u0baa\u0bcd\u0baa\u0bca\u0bb0\u0bc1\u0b9f\u0bcd\u0b95\u0bb3\u0bcd \u0b9a\u0bc7\u0bb0\u0bcd\u0b95\u0bcd\u0b95\u0bb5\u0bc1\u0bae\u0bcd.'],
['worker.add.info','Stock Information','\u0b9a\u0bb0\u0b95\u0bcd\u0b95\u0bc1 \u0ba4\u0b95\u0bb5\u0bb2\u0bcd'],
['worker.hist.page.title','Work History','\u0baa\u0ba3\u0bbf \u0bb5\u0bb0\u0bb2\u0bbe\u0bb1\u0bc1'],
['worker.hist.sub','All work you have completed.','\u0bae\u0bc1\u0b9f\u0bbf\u0ba4\u0bcd\u0ba4 \u0b85\u0ba9\u0bc8\u0ba4\u0bcd\u0ba4\u0bc1 \u0bb5\u0bc7\u0bb2\u0bc8\u0b95\u0bb3\u0bc1\u0bae\u0bcd.'],
['worker.hist.work','Completed Work','\u0bae\u0bc1\u0b9f\u0bbf\u0ba8\u0bcd\u0ba4 \u0bb5\u0bc7\u0bb2\u0bc8'],
['worker.hist.empty','No completed work yet.','\u0bae\u0bc1\u0b9f\u0bbf\u0ba8\u0bcd\u0ba4 \u0bb5\u0bc7\u0bb2\u0bc8 \u0b87\u0bb2\u0bcd\u0bb2\u0bc8.'],
];
let en=fs.readFileSync(ENP,'utf8').replace(/^\uFEFF/,'');
let ta=fs.readFileSync(TAP,'utf8').replace(/^\uFEFF/,'');
for(const [k,e,t] of ADDED){
  if(!en.includes('\n'+k+'=')){ en+='\n'+k+'='+e; }
  if(!ta.includes('\n'+k+'=')){ ta+='\n'+k+'='+t; }
}
fs.writeFileSync(ENP,en,'utf8'); fs.writeFileSync(TAP,ta,'utf8');
console.log('keys added: '+ADDED.length);
