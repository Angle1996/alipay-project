<script lang="ts" setup>
import {list} from "@/api/orders";
import {onMounted, ref} from "vue";
import {pay, refund} from "@/api/pay";
import {ElLoading, ElMessage} from "element-plus";

const tableData = ref([])
const fetchData = async () => {
  const res = await list()
  tableData.value = res.data
}


onMounted(async () => {
  const loading = ElLoading.service({
    lock: true,
    text: '数据加载中...',
    background: 'rgba(0,0,0,0.7)'
  })
  await fetchData()
  loading.close()
})
const payOrder = async (row: any) => {
  // window.open('http://localhost:9090/alipay/pay2?orderNo=' + row.orderNo)
  const res = await pay(row.orderNo)
  const form = res.data
  // 必须要存进localstorage，否则会报错，显示不完全
  localStorage.setItem('callbackHTML', res.data)
  // 读取本地保存的html数据，使用新窗口打开
  const newWin = window.open('', '_blank')
  // 读取本地保存的html数据，使用当前窗口打开
  newWin.document.write(localStorage.getItem('callbackHTML'))
  // 关闭输出流
  newWin.document.close()
  // 清除本地保存的html数据
  localStorage.removeItem('callbackHTML')

}
const refundOrder = async (row: any) => {
  const data = {
    traceNo: row.orderNo,
    totalAmount: row.total,
    subject: row.goodsName,
    alipayTraceNo: row.payNo
  }
  try {
    const res = await refund(data)
    await fetchData()
    ElMessage.success("退款成功")
  } catch (e) {
    console.log(e)
  }

}

</script>

<template>
  <div>

    <el-table :data="tableData" strip>
      <el-table-column align="center" label="序号" prop="id" width="70"></el-table-column>
      <el-table-column label="订单编号" prop="orderNo"></el-table-column>
      <el-table-column label="商品名称" prop="goodsName"></el-table-column>
      <el-table-column align="center" label="商品数量" prop="num"></el-table-column>
      <el-table-column align="center" label="总价格" prop="total"></el-table-column>
      <el-table-column label="创建时间" prop="createTime"></el-table-column>
      <el-table-column label="支付编号" prop="payNo"></el-table-column>
      <el-table-column label="支付时间" prop="payTime"></el-table-column>
      <el-table-column align="center" label="订单状态" prop="status">
        <template v-slot="scope">
          <el-tag v-if="scope.row.status === 0" type="warning">待支付</el-tag>
          <el-tag v-if="scope.row.status === 1" type="success">已支付</el-tag>
          <el-tag v-if="scope.row.status === 2" type="success">已退款</el-tag>
        </template>
      </el-table-column>
      <el-table-column align="center" label="操作">
        <template v-slot="scope">
          <el-button v-if="scope.row.status === 0" size="small" type="primary" @click="payOrder(scope.row)">支付
          </el-button>
          <el-button v-if="scope.row.status === 1" size="small" type="primary" @click="refundOrder(scope.row)">退款
          </el-button>
        </template>
      </el-table-column>
    </el-table>

  </div>
</template>

<style scoped>

</style>