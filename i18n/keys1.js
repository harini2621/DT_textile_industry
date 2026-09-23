const fs=require('fs');
const ENP='src/main/resources/messages.properties';
const TAP='src/main/resources/messages_ta.properties';
const ADDED=[
['owner.dash2.title','Owner Dashboard','\u0b89\u0bb0\u0bbf\u0bae\u0bc8\u0baf\u0bbe\u0bb3\u0bb0\u0bcd \u0b9f\u0bbe\u0bb7\u0bcd\u0baa\u0bcb\u0bb0\u0bcd\u0b9f\u0bc1'],
['owner.dash.welcome.prefix','Welcome back,','\u0bae\u0bc0\u0ba3\u0bcd\u0b9f\u0bc1\u0bae\u0bcd \u0bb5\u0bb0\u0bb5\u0bc7\u0bb1\u0bcd\u0b95\u0bbf\u0bb1\u0bcb\u0bae\u0bcd,'],
['owner.dash.create.order','Create Order','\u0baa\u0bc1\u0ba4\u0bbf\u0baf \u0b86\u0ba3\u0bc8'],
['owner.stat.total.stock','Total Stock (units)','\u0bae\u0bca\u0ba4\u0bcd\u0ba4 \u0b9a\u0bb0\u0b95\u0bcd\u0b95\u0bc1 (\u0b85\u0bb2\u0b95\u0bc1\u0b95\u0bb3\u0bcd)'],
['owner.stat.total.orders','Total Orders','\u0bae\u0bca\u0ba4\u0bcd\u0ba4 \u0b86\u0ba3\u0bc8\u0b95\u0bb3\u0bcd'],
['owner.stat.total.workers','Total Workers','\u0bae\u0bca\u0ba4\u0bcd\u0ba4 \u0baa\u0ba3\u0bbf\u0baf\u0bbe\u0bb3\u0bb0\u0bcd\u0b95\u0bb3\u0bcd'],
['owner.stat.pending.orders','Pending Orders','\u0ba8\u0bbf\u0bb2\u0bc1\u0bb5\u0bc8 \u0b86\u0ba3\u0bc8\u0b95\u0bb3\u0bcd'],
['owner.card.status.overview','Order Status Overview','\u0b86\u0ba3\u0bc8 \u0ba8\u0bbf\u0bb2\u0bc8 \u0b95\u0ba3\u0bcd\u0ba3\u0bcb\u0b9f\u0bcd\u0b9f\u0bae\u0bcd'],
['owner.card.summary','Business Summary','\u0bb5\u0ba3\u0bbf\u0b95 \u0b9a\u0bc1\u0bb0\u0bc1\u0b95\u0bcd\u0b95\u0bae\u0bcd'],
['owner.card.completed.orders','Completed Orders','\u0bae\u0bc1\u0b9f\u0bbf\u0ba8\u0bcd\u0ba4 \u0b86\u0ba3\u0bc8\u0b95\u0bb3\u0bcd'],
['owner.card.recent.orders','Recent Orders','\u0b9a\u0bae\u0bc0\u0baa\u0ba4\u0bcd\u0ba4\u0bbf\u0baf \u0b86\u0ba3\u0bc8\u0b95\u0bb3\u0bcd'],
['owner.empty.no.orders','No orders yet.','\u0b87\u0ba9\u0bcd\u0ba9\u0bc1\u0bae\u0bcd \u0b86\u0ba3\u0bc8\u0b95\u0bb3\u0bcd \u0b87\u0bb2\u0bcd\u0bb2\u0bc8.'],
['owner.btn.create.first','Create First Order','\u0bae\u0bc1\u0ba4\u0bb2\u0bcd \u0b86\u0ba3\u0bc8\u0baf\u0bc8 \u0b89\u0bb0\u0bc1\u0bb5\u0bbe\u0b95\u0bcd\u0b95\u0bc1'],
['owner.card.search','Search Inventory','\u0b9a\u0bb0\u0b95\u0bcd\u0b95\u0bc1 \u0ba4\u0bc7\u0b9f\u0bb2\u0bcd'],
['owner.card.search.desc','Monitor stock availability in real time.','\u0b9a\u0bb0\u0b95\u0bcd\u0b95\u0bc1 \u0b87\u0bb0\u0bc1\u0baa\u0bcd\u0baa\u0bc8 \u0ba8\u0bc7\u0bb0\u0b9f\u0bbf\u0baf\u0bbe\u0b95 \u0b95\u0ba3\u0bcd\u0b95\u0bbe\u0ba3\u0bbf\u0b95\u0bcd\u0b95\u0bb5\u0bc1\u0bae\u0bcd.'],
['owner.card.view.stock','View Stock','\u0b9a\u0bb0\u0b95\u0bcd\u0b95\u0bc8 \u0b95\u0bbe\u0ba3\u0bcd'],
['owner.card.manage','Manage Orders','\u0b86\u0ba3\u0bc8\u0b95\u0bb3\u0bc8 \u0ba8\u0bbf\u0bb0\u0bcd\u0bb5\u0b95\u0bbf'],
['owner.card.manage.desc','Review and track all customer orders.','\u0b85\u0ba9\u0bc8\u0ba4\u0bcd\u0ba4\u0bc1 \u0b86\u0ba3\u0bc8\u0b95\u0bb3\u0bc8\u0baf\u0bc1\u0bae\u0bcd \u0bae\u0ba4\u0bbf\u0baa\u0bcd\u0baa\u0bbe\u0baf\u0bcd\u0bb5\u0bc1 \u0b9a\u0bc6\u0baf\u0bcd\u0ba4\u0bc1 \u0b95\u0ba3\u0bcd\u0b95\u0bbe\u0ba3\u0bbf\u0b95\u0bcd\u0b95\u0bb5\u0bc1\u0bae\u0bcd.'],
['owner.card.open.orders','Open Orders','\u0b86\u0ba3\u0bc8\u0b95\u0bb3\u0bc8 \u0ba4\u0bbf\u0bb1'],
['owner.card.biz.reports','Business Reports','\u0bb5\u0ba3\u0bbf\u0b95 \u0b85\u0bb1\u0bbf\u0b95\u0bcd\u0b95\u0bc8\u0b95\u0bb3\u0bcd'],
['owner.card.biz.desc','Analyze production and business performance.','\u0b89\u0bb1\u0bcd\u0baa\u0ba4\u0bcd\u0ba4\u0bbf \u0bae\u0bb1\u0bcd\u0bb1\u0bc1\u0bae\u0bcd \u0bb5\u0ba3\u0bbf\u0b95 \u0b9a\u0bc6\u0baf\u0bb2\u0bcd\u0ba4\u0bbf\u0bb1\u0ba9\u0bc8 \u0baa\u0b95\u0bc1\u0baa\u0bcd\u0baa\u0bbe\u0baf\u0bcd\u0bb5\u0bc1 \u0b9a\u0bc6\u0baf\u0bcd\u0b95.'],
['owner.card.view.reports','View Reports','\u0b85\u0bb1\u0bbf\u0b95\u0bcd\u0b95\u0bc8\u0b95\u0bb3\u0bc8 \u0b95\u0bbe\u0ba3\u0bcd'],
['owner.orders.page.title','Order Management','\u0b86\u0ba3\u0bc8 \u0bae\u0bc7\u0bb2\u0bbe\u0ba3\u0bcd\u0bae\u0bc8'],
];
let en=fs.readFileSync(ENP,'utf8').replace(/^\uFEFF/,'');
let ta=fs.readFileSync(TAP,'utf8').replace(/^\uFEFF/,'');
for(const [k,e,t] of ADDED){
  if(!en.includes('\n'+k+'=')){ en+='\n'+k+'='+e; }
  if(!ta.includes('\n'+k+'=')){ ta+='\n'+k+'='+t; }
}
fs.writeFileSync(ENP,en,'utf8'); fs.writeFileSync(TAP,ta,'utf8');
console.log('keys added: '+ADDED.length);
