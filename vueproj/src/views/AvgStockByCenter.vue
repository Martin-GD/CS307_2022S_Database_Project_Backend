<template>
  <div class="common-layout">
    <el-table :data="tableData"  align="center" stripe="true">
      <el-table-column prop="supply_center" label="supply_center" width="400" />
      <el-table-column prop="average" label="average" width="200" />

      <!--      <el-table-column prop="address" label="Address" />-->
    </el-table>
    <!--    <el-input v-model="input" placeholder="Please input" />-->
  </div>
</template>

<script>
import axios from 'axios'

export default {
  data(){
    return{
      msg:'hello world!',
      tableData : []
    }
  },
  created() {
    const _this=this
    axios.get("http://10.26.142.228:8181/getAvgStockByCenter").then(function (resp){
      _this.tableData = resp.data
    })
  }
}
</script>