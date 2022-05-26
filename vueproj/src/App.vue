<!--<template>-->
<!--  <nav>-->
<!--    <router-link to="/">Home</router-link> |-->
<!--    <router-link to="/about">AllStaffCount</router-link>-->
<!--  </nav>-->
<!--  <router-view/>-->
<!--  <div>-->

<!--  </div>-->
<!--</template>-->


<!--<style>-->
<!--#app {-->
<!--  font-family: Avenir, Helvetica, Arial, sans-serif;-->
<!--  -webkit-font-smoothing: antialiased;-->
<!--  -moz-osx-font-smoothing: grayscale;-->
<!--  text-align: center;-->
<!--  color: #2c3e50;-->
<!--}-->

<!--nav {-->
<!--  padding: 30px;-->
<!--}-->

<!--nav a {-->
<!--  font-weight: bold;-->
<!--  color: #2c3e50;-->
<!--}-->

<!--nav a.router-link-exact-active {-->
<!--  color: #ea9c22;-->
<!--}-->
<!--</style>-->

<template>
  <div>

    <div class="h-6" />
    <el-menu
        :default-active="activeIndex2"
        class="el-menu-demo"
        mode="horizontal"
        background-color="#86bef3"
        text-color="#fff"
        active-text-color="#ffd04b"
        @select="handleSelect"
    >
      <el-menu-item index="1"><router-link to="/">Home</router-link></el-menu-item>
      <el-menu-item index="2"><router-link to="/about">AllStaffCount</router-link></el-menu-item>

      <el-sub-menu index="2">
        <template #title>Check</template>
        <el-menu-item index="2-1"><router-link to="/about">AllStaffCount</router-link></el-menu-item>
        <el-menu-item index="2-2"><router-link to="/ContractCount">ContractCount</router-link></el-menu-item>
        <el-menu-item index="2-3"><router-link to="/OrderCount">OrderCount</router-link></el-menu-item>
        <el-menu-item index="2-4"><router-link to="/NeverSoldProductCount">NeverSoldProductCount</router-link></el-menu-item>
        <el-menu-item index="2-5"><router-link to="/FavoriteProductModel">FavoriteProductModel</router-link></el-menu-item>
        <el-menu-item index="2-6"><router-link to="/AvgStockByCenter">AvgStockByCenter</router-link></el-menu-item>
        <el-menu-item index="2-7"><router-link to="/ProductByNumber">ProductByNumber</router-link></el-menu-item>
        <el-menu-item index="2-8"><router-link to="/ContractInfo">ContractInfo</router-link></el-menu-item>
        <el-menu-item index="2-5">item three</el-menu-item>
        <el-sub-menu index="2-4">
          <template #title>item four</template>
          <el-menu-item index="2-4-1">item one</el-menu-item>
          <el-menu-item index="2-4-2">item two</el-menu-item>
          <el-menu-item index="2-4-3">item three</el-menu-item>
        </el-sub-menu>
      </el-sub-menu>
      <el-menu-item index="3" disabled>Info</el-menu-item>
      <el-menu-item index="4">Orders</el-menu-item>
    </el-menu>

      <div>
        <router-view/>
      </div>

  </div>

</template>
