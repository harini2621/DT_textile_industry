# -*- coding: utf-8 -*-
"""Phase-2 key block + template applier (ASCII-only file; Tamil via escapes)."""
import os
BASE = r'c:\Users\HP\DT_textile_industry\src\main\resources'
TPL = os.path.join(BASE, 'templates')
ENP = os.path.join(BASE, 'messages.properties')
TAP = os.path.join(BASE, 'messages_ta.properties')
ADDED = [
 ('owner.dashboard.heading2','Owner Dashboard','\u0b89\u0bb0\u0bbf\u0bae\u0bc8\u0baf\u0bbe\u0bb3\u0bb0\u0bcd \u0b9f\u0bbe\u0bb7\u0bcd\u0baa\u0bcb\u0bb0\u0bcd\u0b9f\u0bc1'),
 ('owner.dash.welcome.prefix','Welcome back,','\u0bae\u0bc0\u0ba3\u0bcd\u0b9f\u0bc1\u0bae\u0bcd \u0bb5\u0bb0\u0bb5\u0bc7\u0bb1\u0bcd\u0b95\u0bbf\u0bb1\u0bcb\u0bae\u0bcd,'),
 ('owner.createorder.title','Create New Order','\u0baa\u0bc1\u0ba4\u0bbf\u0baf \u0b86\u0ba3\u0bc8 \u0b89\u0bb0\u0bc1\u0bb5\u0bbe\u0b95\u0bcd\u0b95\u0bc1'),
 ('owner.createorder.sub','Fill in the order details below.','\u0b95\u0bc0\u0bb4\u0bc7 \u0b86\u0ba3\u0bc8 \u0bb5\u0bbf\u0bb5\u0bb0\u0b99\u0bcd\u0b95\u0bb3\u0bc8 \u0ba8\u0bbf\u0bb0\u0baa\u0bcd\u0baa\u0bb5\u0bc1\u0bae\u0bcd.'),
 ('owner.createorder.details','Order Details','\u0b86\u0ba3\u0bc8 \u0bb5\u0bbf\u0bb5\u0bb0\u0b99\u0bcd\u0b95\u0bb3\u0bcd'),
 ('form.product.name','Product Name','\u0ba4\u0baf\u0bbe\u0bb0\u0bbf\u0baa\u0bcd\u0baa\u0bc1 \u0baa\u0bc6\u0baf\u0bb0\u0bcd'),
 ('form.quantity','Quantity','\u0b85\u0bb3\u0bb5\u0bc1'),
 ('form.assign.worker','Assign to Worker (Username)','\u0baa\u0ba3\u0bbf\u0baf\u0bbe\u0bb3\u0bb0\u0bc1\u0b95\u0bcd\u0b95\u0bc1 \u0b92\u0ba4\u0bc1\u0b95\u0bcd\u0b95\u0bc1 (\u0baa\u0baf\u0ba9\u0bb0\u0bcd\u0baa\u0bc6\u0baf\u0bb0\u0bcd)'),
 ('form.delivery.date','Expected Delivery Date','\u0b8e\u0ba4\u0bbf\u0bb0\u0bcd\u0baa\u0bbe\u0bb0\u0bcd\u0b95\u0bcd\u0b95\u0baa\u0bcd\u0baa\u0b9f\u0bc1\u0bae\u0bcd \u0bb5\u0bbf\u0ba8\u0bbf\u0baf\u0bcb\u0b95 \u0ba4\u0bc7\u0ba4\u0bbf'),
 ('form.remarks','Remarks','\u0b95\u0bc1\u0bb1\u0bbf\u0baa\u0bcd\u0baa\u0bc1\u0b95\u0bb3\u0bcd'),
]
