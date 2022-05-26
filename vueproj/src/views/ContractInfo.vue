<template>
  <div class="common-layout">
    <el-input v-model="input" placeholder="like CSE0000106"></el-input>
    <el-button @click="handle_click" type="success">search</el-button>
    <el-table :data="tableData"  align="center" stripe="true">
      <el-table-column prop="number" label="number" width="200" />
      <el-table-column prop="manager" label="manager" width="200" />
      <el-table-column prop="enterprise" label="enterprise" width="400" />
      <el-table-column prop="supply_center" label="supply_center" width="300" />
    </el-table>
    <el-table :data="tableData2"  align="center" stripe="true">
      <el-table-column prop="product_model" label="product_model" width="200" />
      <el-table-column prop="salesman" label="salesman" width="200" />
      <el-table-column prop="quantity" label="quantity" width="200" />
      <el-table-column prop="unit_price" label="unit_price" width="200" />
      <el-table-column prop="estimate_delivery_date" label="estimate_delivery_date" width="200" />
      <el-table-column prop="lodgement_date" label="lodgement_date" width="200" />
    </el-table>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  data(){
    return{
      msg:'hello world!',
      tableData : [],
      tableData2 : [],
      input: ''
    }
  },methods:{
    handle_click() {
      const _this=this
      const url = "http://10.26.142.228:8181/getContractInfo?contract_number=" + _this.input;
      axios.get(url).then(function (resp){
        _this.tableData = resp.data[0]
        _this.tableData2 = resp.data[1]
        console.log(resp.data)
      })
    }
  },
  created() {

  }
}
</script>