### 思路如下

1. 在spring容器初始化时获取zookeeper中数据并加载到Environment中；
   - 在spring.factories中定义容器初始化扩展点，spring容器初始化接口为ApplicationContextInitializer
   - 在容器初始化时加载自定义zookeeper数据加载扩展点，自定义扩展点接口为PropertySourceLocator
   - 此时已实现zookeeper数据加载，但未实现动态更新
2. 加载完成后开启zookeeper数据变更监听，如果数据变更则更新Environment；
   - 此时已实现Environment容器动态变更，但spring容器初始化bean为单例，bean中属性未变更
3. Environment更新完成后发送事件更新bean属性
   - 将需要动态更新的bean上加@RefreshScope自定义注解标记
   - 在spring初始化bean的时候记录带有@RefreshScope注解的bean，存在自定义FiledPair容器中
   - 收到更新bean的事件，则更新FiledPair容器中所有的bean