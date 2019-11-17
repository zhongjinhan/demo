package org.apache.spark.demo

import org.apache.spark.{SecurityManager, SparkConf}
import org.apache.spark.rpc.RpcEnv

object DemoServer {

  def main(args: Array[String]): Unit = {
    process()
  }

  def process(): Unit ={
    val sparkConf = new SparkConf()
    val securityManager = new SecurityManager(sparkConf)
    val rpcEnv = RpcEnv.create("test-env", "127.0.0.1", 10000, sparkConf, securityManager, clientMode = false)
    val echoRpcEndpointRef = rpcEnv.setupEndpoint(EchoRpcEndpoint.ENDPOINT_NAME, new EchoRpcEndpoint(rpcEnv))

    Thread.sleep(100000)
    rpcEnv.stop(echoRpcEndpointRef)
    rpcEnv.shutdown()
  }

}
