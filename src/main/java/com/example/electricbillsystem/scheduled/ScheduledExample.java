package com.example.electricbillsystem.scheduled;

import com.example.electricbillsystem.CustomException;
import com.example.electricbillsystem.controller.InvoiceController;
import com.example.electricbillsystem.model.Invoice;
import com.example.electricbillsystem.service.CustomerService;
import com.example.electricbillsystem.service.ElectricMeterService;
import com.example.electricbillsystem.service.InvoiceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.List;

@Component
@EnableScheduling
@Slf4j
public class ScheduledExample {

    private ThreadPoolTaskScheduler taskScheduler;
    private ElectricMeterService electricMeterService;
    private InvoiceController invoiceController;
    private CustomerService customerService;
    private InvoiceService invoiceService;

    int x = 4;

    @Autowired
    public ScheduledExample(ThreadPoolTaskScheduler taskScheduler, ElectricMeterService electricMeterService, InvoiceController invoiceController, CustomerService customerService, InvoiceService invoiceService) {
        this.taskScheduler = taskScheduler;
        this.electricMeterService = electricMeterService;
        this.invoiceController = invoiceController;
        this.customerService = customerService;
        this.invoiceService = invoiceService;
    }

    @PostConstruct
    public void scheduleRunnableWithCronTrigger() {
        taskScheduler.schedule(new RunnableTask("Current Date"), new Date());
        taskScheduler.scheduleWithFixedDelay(new RunnableTask("Fixed 100 second Delay"), 100000);
        taskScheduler.scheduleWithFixedDelay(new RunnableTask("Current Date Fixed 100 second Delay"), new Date(), 100000);
    }

    @Scheduled(fixedDelayString = "${fixedDelayTiming}")
    public void fixedDelay() throws CustomException {
        System.out.println("10 capacity added, FIXED DELAY");
        electricMeterService.addCapacity(3, 10.0);
    }

    @Scheduled(fixedRate = 1000)
    public void fixedRate() throws CustomException {
        System.out.println("10 capacity added, FIXED RATE");
        electricMeterService.addCapacity(3, 10.0);
    }

    @Scheduled(initialDelay = 6000, fixedDelay = 6000)
    public void initialDelay() throws CustomException {
        System.out.println("Invoice has been paid");
        customerService.payPaypal(3, x, 1, 800.0);
        x++;
        List<Invoice> invoiceList = invoiceService.getUsersInvoices(3);
        for (Invoice i : invoiceList) {
            System.out.println(i);
        }
    }

    @Scheduled(cron = "${cron.expression}")
    public void cronExpression() {
        System.out.println("Invoice has been created");
        invoiceController.addInvoice(new Invoice(), 3, 1);
    }

    class RunnableTask implements Runnable {

        private String message;

        public RunnableTask(String message) {
            this.message = message;
        }

        @Override
        public void run() {
            System.out.println("Runnable Task with " + message + " on thread " + Thread.currentThread().getName());
        }
    }

}
