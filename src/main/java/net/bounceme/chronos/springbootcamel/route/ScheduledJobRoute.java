package net.bounceme.chronos.springbootcamel.route;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class ScheduledJobRoute extends RouteBuilder {

    @Override
    public void configure() {
        from("quartz:scheduled-job?cron=0/30+*+*+*+*+?")
                .routeId("scheduled-job-route")
                .log("Job programado ejecutado: ${date:now:yyyy-MM-dd'T'HH:mm:ss}");
    }
}
