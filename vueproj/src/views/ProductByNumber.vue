<template>
  <div class="common-layout">
    <el-input v-model="input" placeholder="like A50L172"></el-input>
    <el-button @click="handle_click" type="success">search</el-button>

    <el-table :data="tableData"  align="center" stripe="true">
      <el-table-column prop="supply_center" label="supply_center" width="400" />
      <el-table-column prop="product_name" label="product_name" width="200" />
      <el-table-column prop="model_name" label="model_name" width="200" />
      <el-table-column prop="purchase_price" label="purchase_price" width="200" />
      <el-table-column prop="quantity" label="quantity" width="200" />
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

      input: ''
    }
  },methods:{
    handle_click() {
      const _this=this
      const url = "http://10.26.142.228:8181/getProductByNumber?product_number=" + _this.input;
      axios.get(url).then(function (resp){
        _this.tableData = resp.data

        console.log(resp.data)
      })
    }
  },
  created() {

  }
}
</script>