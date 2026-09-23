const {load,save} = require('./lib');
const F='owner-payment-tracking.html';
let t = load(F);
t = t.split('</i>Record New Payment</h5>').join('</i><span th:text="#{payment.record.title}">Record New Payment</span></h5>');
t = t.split('<option value="">Select Order</option>').join('<option value="" th:text="#{owner.pay.select.order}">Select Order</option>');
t = t.split('<button type="submit" class="btn btn-success rounded-pill px-4">Save</button>').join('<button type="submit" class="btn btn-success rounded-pill px-4" th:text="#{common.save}">Save</button>');
save(F,t); console.log('pay multiline fixed');
