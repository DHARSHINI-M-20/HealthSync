package com.healthsync.decorator;
/** Base decorator for optional appointment services. */
public abstract class AppointmentServiceDecorator implements AppointmentServiceOption {
    protected final AppointmentServiceOption delegate;
    protected AppointmentServiceDecorator(AppointmentServiceOption delegate) { this.delegate=delegate; }
}
