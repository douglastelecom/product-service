package com.example.productservice.connections;

import jakarta.annotation.PostConstruct;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQConnection {

    public AmqpAdmin amqpAdmin;

    public RabbitMQConnection(AmqpAdmin amqpAdmin) {
        this.amqpAdmin = amqpAdmin;
    }
    private Queue createQueue(String nomeFila){
        return new Queue(nomeFila, true, false, false);
    }
    private DirectExchange createExchange(String nomeExchange){
        return new DirectExchange(nomeExchange);
    }
    private Binding createBinding(Queue queue, DirectExchange directExchange, String routingKey){
        return new Binding(queue.getName(), Binding.DestinationType.QUEUE, directExchange.getName(), routingKey, null);
    }

    @PostConstruct
    private void criarFilaProductsInventario(){

        configRabbit("product-inventory-save", "amq.direct","product-inventory-save");
        configRabbit("product-inventory-getall", "amq.direct","product-inventory-getall");
    }

    private void configRabbit(String queueName, String exchangeName, String routingKey){
        Queue queue = createQueue(queueName);
        DirectExchange directExchange = createExchange(exchangeName);
        Binding binding = createBinding(queue, directExchange, routingKey);
        amqpAdmin.declareQueue(queue);
        amqpAdmin.declareExchange(directExchange);
        amqpAdmin.declareBinding(binding);
    }

}
