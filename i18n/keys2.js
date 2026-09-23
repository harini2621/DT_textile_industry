const fs=require('fs');
const ENP='src/main/resources/messages.properties';
const TAP='src/main/resources/messages_ta.properties';
const ADDED=[
['owner.orders.title','Order Management','\u0b86\u0ba3\u0bc8 \u0bae\u0bc7\u0bb2\u0bbe\u0ba3\u0bcd\u0bae\u0bc8'],
['owner.orders.sub','Track and manage your textile orders.','\u0b89\u0b99\u0bcd\u0b95\u0bb3\u0bcd \u0b9c\u0bb5\u0bc1\u0bb3\u0bbf \u0b86\u0ba3\u0bc8\u0b95\u0bb3\u0bc8 \u0b95\u0ba3\u0bcd\u0b95\u0bbe\u0ba3\u0bbf\u0ba4\u0bcd\u0ba4\u0bc1 \u0ba8\u0bbf\u0bb0\u0bcd\u0bb5\u0b95\u0bbf\u0b95\u0bcd\u0b95\u0bb5\u0bc1\u0bae\u0bcd.'],
['owner.orders.new','New Order','\u0baa\u0bc1\u0ba4\u0bbf\u0baf \u0b86\u0ba3\u0bc8'],
['owner.orders.my','My Orders','\u0b8e\u0ba9\u0bcd \u0b86\u0ba3\u0bc8\u0b95\u0bb3\u0bcd'],
['owner.orders.empty','No orders found.','\u0b86\u0ba3\u0bc8\u0b95\u0bb3\u0bcd \u0b8e\u0ba4\u0bc1\u0bb5\u0bc1\u0bae\u0bcd \u0b87\u0bb2\u0bcd\u0bb2\u0bc8.'],
['owner.orders.search.ph','Search order...','\u0b86\u0ba3\u0bc8\u0baf\u0bc8 \u0ba4\u0bc7\u0b9f\u0bc1\u0b95...'],
['owner.orders.create','Create Order','\u0b86\u0ba3\u0bc8\u0baf\u0bc8 \u0b89\u0bb0\u0bc1\u0bb5\u0bbe\u0b95\u0bcd\u0b95\u0bc1'],
['owner.search.title','Search Inventory','\u0b9a\u0bb0\u0b95\u0bcd\u0b95\u0bc1 \u0ba4\u0bc7\u0b9f\u0bb2\u0bcd'],
['owner.search.sub','Search and monitor textile stock instantly.','\u0b9c\u0bb5\u0bc1\u0bb3\u0bbf \u0b9a\u0bb0\u0b95\u0bcd\u0b95\u0bc8 \u0b89\u0b9f\u0ba9\u0b9f\u0bbf\u0baf\u0bbe\u0b95 \u0ba4\u0bc7\u0b9f\u0bbf \u0b95\u0ba3\u0bcd\u0b95\u0bbe\u0ba3\u0bbf\u0b95\u0bcd\u0b95\u0bb5\u0bc1\u0bae\u0bcd.'],
['owner.search.by.name','Search by Product Name','\u0ba4\u0baf\u0bbe\u0bb0\u0bbf\u0baa\u0bcd\u0baa\u0bc1 \u0baa\u0bc6\u0baf\u0bb0\u0bbe\u0bb2\u0bcd \u0ba4\u0bc7\u0b9f\u0bc1\u0b95'],
['owner.search.results','Inventory Results','\u0b9a\u0bb0\u0b95\u0bcd\u0b95\u0bc1 \u0bae\u0bc1\u0b9f\u0bbf\u0bb5\u0bc1\u0b95\u0bb3\u0bcd'],
['owner.search.no.stock','No stock found','\u0b9a\u0bb0\u0b95\u0bcd\u0b95\u0bc1 \u0b8e\u0ba4\u0bc1\u0bb5\u0bc1\u0bae\u0bcd \u0b87\u0bb2\u0bcd\u0bb2\u0bc8'],
['owner.search.search','Search','\u0ba4\u0bc7\u0b9f\u0bc1'],
['owner.create.title','Create New Order','\u0baa\u0bc1\u0ba4\u0bbf\u0baf \u0b86\u0ba3\u0bc8 \u0b89\u0bb0\u0bc1\u0bb5\u0bbe\u0b95\u0bcd\u0b95\u0bc1'],
['owner.create.sub','Fill in the order details below.','\u0b95\u0bc0\u0bb4\u0bc7 \u0b86\u0ba3\u0bc8 \u0bb5\u0bbf\u0bb5\u0bb0\u0b99\u0bcd\u0b95\u0bb3\u0bc8 \u0ba8\u0bbf\u0bb0\u0baa\u0bcd\u0baa\u0bb5\u0bc1\u0bae\u0bcd.'],
['owner.create.details','Order Details','\u0b86\u0ba3\u0bc8 \u0bb5\u0bbf\u0bb5\u0bb0\u0b99\u0bcd\u0b95\u0bb3\u0bcd'],
['form.product','Product Name','\u0ba4\u0baf\u0bbe\u0bb0\u0bbf\u0baa\u0bcd\u0baa\u0bc1 \u0baa\u0bc6\u0baf\u0bb0\u0bcd'],
['form.qty','Quantity','\u0b85\u0bb3\u0bb5\u0bc1'],
['form.assign.worker','Assign to Worker (Username)','\u0baa\u0ba3\u0bbf\u0baf\u0bbe\u0bb3\u0bb0\u0bc1\u0b95\u0bcd\u0b95\u0bc1 \u0b92\u0ba4\u0bc1\u0b95\u0bcd\u0b95\u0bc1 (\u0baa\u0baf\u0ba9\u0bb0\u0bcd\u0baa\u0bc6\u0baf\u0bb0\u0bcd)'],
['form.delivery','Expected Delivery Date','\u0b8e\u0ba4\u0bbf\u0bb0\u0bcd\u0baa\u0bbe\u0bb0\u0bcd\u0b95\u0bcd\u0b95\u0baa\u0bcd\u0baa\u0b9f\u0bc1\u0bae\u0bcd \u0bb5\u0bbf\u0ba8\u0bbf\u0baf\u0bcb\u0b95 \u0ba4\u0bc7\u0ba4\u0bbf'],
['form.remarks','Remarks','\u0b95\u0bc1\u0bb1\u0bbf\u0baa\u0bcd\u0baa\u0bc1\u0b95\u0bb3\u0bcd'],
['owner.create.submit','Create Order','\u0b86\u0ba3\u0bc8\u0baf\u0bc8 \u0b89\u0bb0\u0bc1\u0bb5\u0bbe\u0b95\u0bcd\u0b95\u0bc1'],
];
let en=fs.readFileSync(ENP,'utf8').replace(/^\uFEFF/,'');
let ta=fs.readFileSync(TAP,'utf8').replace(/^\uFEFF/,'');
for(const [k,e,t] of ADDED){
  if(!en.includes('\n'+k+'=')){ en+='\n'+k+'='+e; }
  if(!ta.includes('\n'+k+'=')){ ta+='\n'+k+'='+t; }
}
fs.writeFileSync(ENP,en,'utf8'); fs.writeFileSync(TAP,ta,'utf8');
console.log('keys added: '+ADDED.length);
