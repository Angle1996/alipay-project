<script lang="ts" setup>
import {onMounted, ref} from "vue";
import {list} from "@/api/goods";
import {add} from "@/api/orders";
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
const buy = async (row: any) => {
  const data = {goodsName: row.name, num: 1, total: row.price * 1}
  try {
    await add(data)
    ElMessage.success('下单成功！请支付')
  } catch (e) {
    ElMessage.error('下单失败！')
  }

}
</script>

<template>
  <div>

    <el-table :data="tableData" strip>
      <el-table-column align="center" label="序号" prop="id" width="70"></el-table-column>
      <el-table-column label="商品名称" prop="name"></el-table-column>
      <el-table-column label="商品编号" prop="no"></el-table-column>
      <el-table-column label="商品价格" prop="price"></el-table-column>
      <el-table-column label="生产日期" prop="date"></el-table-column>
      <el-table-column align="center" label="操作">
        <template v-slot="scope">
          <el-button size="small" type="primary" @click="buy(scope.row)">购买</el-button>
        </template>
      </el-table-column>
    </el-table>

  </div>
</template>

<style scoped>

</style>