<template>
  <div class="common-layout">
    <el-table :data="tableData"  align="center" stripe="true">
      <el-table-column prop="number" label="ContractCount" width="200" />
    </el-table>

  </div>
</template>

<script>
import axios from 'axios'

export default {
  data(){
    return{
      tableData : [{
          number:0
      }]
    }
  },
  created() {
    const _this=this
    axios.get("http://10.26.142.228:8181/getContractCount").then(function (resp){
      _this.tableData[0].number = resp.data
    })
  }
}

</script>

<style scoped>

</style>