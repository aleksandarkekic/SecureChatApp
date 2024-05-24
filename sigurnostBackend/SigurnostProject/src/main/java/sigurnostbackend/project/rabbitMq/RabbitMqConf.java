package sigurnostbackend.project.rabbitMq;

import org.springframework.amqp.core.*;

import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactoryContextWrapper;
import org.springframework.amqp.rabbit.connection.SimpleRoutingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.Map;

@Configuration
public class RabbitMqConf {
    @Value("${spring.rabbitmq.server1.hostname}")
    private String hostName1;

    @Value("${spring.rabbitmq.server2.hostname}")
    private String hostName2;

    @Value("${spring.rabbitmq.server3.hostname}")
    private String hostName3;

    @Value("${spring.rabbitmq.server4.hostname}")
    private String hostName4;

    @Value("${spring.rabbitmq.server1.host}")
    private String host1;

    @Value("${spring.rabbitmq.server2.host}")
    private String host2;

    @Value("${spring.rabbitmq.server3.host}")
    private String host3;

    @Value("${spring.rabbitmq.server4.host}")
    private String host4;

    @Value("${spring.rabbitmq.server1.username}")
    private String username1;

    @Value("${spring.rabbitmq.server2.username}")
    private String username2;

    @Value("${spring.rabbitmq.server3.username}")
    private String username3;

    @Value("${spring.rabbitmq.server4.username}")
    private String username4;

    @Value("${spring.rabbitmq.server1.password}")
    private String pass1;

    @Value("${spring.rabbitmq.server2.password}")
    private String pass2;

    @Value("${spring.rabbitmq.server3.password}")
    private String pass3;

    @Value("${spring.rabbitmq.server4.password}")
    private String pass4;

    @Value("${spring.rabbitmq.server1.port}")
    private int port1;

    @Value("${spring.rabbitmq.server2.port}")
    private int port2;

    @Value("${spring.rabbitmq.server3.port}")
    private int port3;

    @Value("${spring.rabbitmq.server4.port}")
    private int port4;

    @Value("${rabbitmq.queue.name1}")
    private String queue1;

    @Value("${rabbitmq.queue.name2}")
    private String queue2;

    @Value("${rabbitmq.queue.name3}")
    private String queue3;

    @Value("${rabbitmq.queue.name4}")
    private String queue4;

    @Value("${rabbitmq.exchange.name1}")
    private String exchange1;

    @Value("${rabbitmq.exchange.name2}")
    private String exchange2;

    @Value("${rabbitmq.exchange.name3}")
    private String exchange3;

    @Value("${rabbitmq.exchange.name4}")
    private String exchange4;

    @Value("${rabbitmq.routingkey.name1}")
    private String key1;

    @Value("${rabbitmq.routingkey.name2}")
    private String key2;

    @Value("${rabbitmq.routingkey.name3}")
    private String key3;

    @Value("${rabbitmq.routingkey.name4}")
    private String key4;

    @Bean
    @Primary
    @Qualifier("server1")
    public ConnectionFactory connectionFactory1() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setHost(host1);
        connectionFactory.setPort(port1);
        connectionFactory.setUsername(username1);
        connectionFactory.setPassword(pass1);

        return connectionFactory;
    }

    @Bean
    @Qualifier("server2")
    public ConnectionFactory connectionFactory2() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setHost(host2);
        connectionFactory.setPort(port2);
        connectionFactory.setUsername(username2);
        connectionFactory.setPassword(pass2);
        return connectionFactory;
    }

    @Bean
    @Qualifier("server3")
    public ConnectionFactory connectionFactory3() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setHost(host3);
        connectionFactory.setPort(port3);
        connectionFactory.setUsername(username3);
        connectionFactory.setPassword(pass3);
        return connectionFactory;
    }

    @Bean
    @Qualifier("server4")
    public ConnectionFactory connectionFactory4() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setHost(host4);
        connectionFactory.setPort(port4);
        connectionFactory.setUsername(username4);
        connectionFactory.setPassword(pass4);
        return connectionFactory;
    }
    @Bean
    SimpleRoutingConnectionFactory rcf(
            @Qualifier("server1") ConnectionFactory connectionFactory1,
            @Qualifier("server2") ConnectionFactory connectionFactory2,
            @Qualifier("server3") ConnectionFactory connectionFactory3,
            @Qualifier("server4") ConnectionFactory connectionFactory4) {

        SimpleRoutingConnectionFactory rcf = new SimpleRoutingConnectionFactory();
        rcf.setDefaultTargetConnectionFactory(connectionFactory1);
        rcf.setTargetConnectionFactories(Map.of("one", connectionFactory1, "two", connectionFactory2, "three", connectionFactory3,"four",connectionFactory4));
        return rcf;
    }
    @Bean
    public MessageConverter converter(){
        return new Jackson2JsonMessageConverter();
    }
    
    @Bean
    @Qualifier("server1")
    public RabbitTemplate amqpTemplate1(@Qualifier("server1") ConnectionFactory connectionFactory){
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(converter());
       rabbitTemplate.setExchange(exchange1);

        return  rabbitTemplate;
    }

    @Bean
    @Qualifier("server2")
    public RabbitTemplate amqpTemplate2(@Qualifier("server2") ConnectionFactory connectionFactory){
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(converter());
        rabbitTemplate.setExchange(exchange2);

        return  rabbitTemplate;
    }

    @Bean
    @Qualifier("server3")
    public RabbitTemplate amqpTemplate3(@Qualifier("server3") ConnectionFactory connectionFactory){
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(converter());
        rabbitTemplate.setExchange(exchange3);


        return  rabbitTemplate;
    }

    @Bean
    @Qualifier("server4")
    public RabbitTemplate amqpTemplate4(@Qualifier("server4") ConnectionFactory connectionFactory){
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(converter());
        rabbitTemplate.setExchange(exchange4);

        return  rabbitTemplate;
    }


    @Bean
    @Qualifier("server1")
    public RabbitAdmin rabbitAdmin1(@Qualifier("server1") ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }

    @Bean
    @Qualifier("server2")
    public RabbitAdmin rabbitAdmin2(@Qualifier("server2") ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }

    @Bean
    @Qualifier("server3")
    public RabbitAdmin rabbitAdmin3(@Qualifier("server3") ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }

    @Bean
    @Qualifier("server4")
    public RabbitAdmin rabbitAdmin4(@Qualifier("server4") ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }

    @Bean
    @Qualifier("server1")
    public Queue myQueue1() {
        return new Queue(queue1);
    }

    @Bean
    @Qualifier("server2")
    public Queue myQueue2() {
        return new Queue(queue2);
    }

    @Bean
    @Qualifier("server3")
    public Queue myQueue3() {
        return new Queue(queue3);
    }

    @Bean
    @Qualifier("server4")
    public Queue myQueue4() {
        return new Queue(queue4);
    }

    @Bean
    @Qualifier("server1")
    public TopicExchange exchange1() {
        return new TopicExchange(exchange1);
    }

    @Bean
    @Qualifier("server2")
    public TopicExchange exchange2() {
        return new TopicExchange(exchange2);
    }

    @Bean
    @Qualifier("server3")
    public TopicExchange exchange3() {
        return new TopicExchange(exchange3);
    }

    @Bean
    @Qualifier("server4")
    public TopicExchange exchange4() {
        return new TopicExchange(exchange4);
    }

    @Bean
    @Qualifier("server1")
    public Binding binding1() {
        return BindingBuilder.
                bind(myQueue1()).
                to(exchange1()).
                with(key1);
    }

    @Bean
    @Qualifier("server2")
    public Binding binding2() {
        return BindingBuilder.
                bind(myQueue2()).
                to(exchange2()).
                with(key2);
    }

    @Bean
    @Qualifier("server3")
    public Binding binding3() {
        return BindingBuilder.
                bind(myQueue3()).
                to(exchange3()).
                with(key3);
    }

    @Bean
    @Qualifier("server4")
    public Binding binding4() {
        return BindingBuilder.
                bind(myQueue4()).
                to(exchange4()).
                with(key4);
    }
    @Bean
    public SimpleRabbitListenerContainerFactory factory1(@Qualifier("server1") ConnectionFactory cf1) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(cf1);
        factory.setMessageConverter(converter());

        return factory;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory factory2(@Qualifier("server2") ConnectionFactory cf2) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(cf2);
        factory.setMessageConverter(converter());

        return factory;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory factory3(@Qualifier("server3") ConnectionFactory cf3) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(cf3);
        factory.setMessageConverter(converter());
        return factory;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory factory4(@Qualifier("server4") ConnectionFactory cf4) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(cf4);
        factory.setMessageConverter(converter());
        return factory;
    }
    @Bean
    RabbitTemplate template(SimpleRoutingConnectionFactory rcf) {
        return new RabbitTemplate(rcf);
    }

    @Bean
    ConnectionFactoryContextWrapper wrapper(SimpleRoutingConnectionFactory rcf) {
        return new ConnectionFactoryContextWrapper(rcf);
    }
//    @Bean
//    RabbitTemplate template(SimpleRoutingConnectionFactory rcf) {
//        return new RabbitTemplate(rcf);
//    }
//
//    @Bean
//    ConnectionFactoryContextWrapper wrapper(SimpleRoutingConnectionFactory rcf) {
//        return new ConnectionFactoryContextWrapper(rcf);
//    }
}








