<template>
  <div class="common-layout">
    <el-table :data="tableData"  align="center" stripe="true">
      <el-table-column prop="type" label="staff_type" width="200" />
      <el-table-column prop="number" label="number" width="200" />
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
    axios.get("http://10.26.142.228:8181/getAllStaffCount").then(function (resp){
        _this.tableData = resp.data
    })
  }
}
</script>