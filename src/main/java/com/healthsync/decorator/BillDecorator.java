package com.healthsync.decorator;
/** Base decorator for optional invoice adjustments. */
public abstract class BillDecorator implements BillComponent { protected final BillComponent delegate; protected BillDecorator(BillComponent delegate) { this.delegate = delegate; } }
