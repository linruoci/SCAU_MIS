<template>
    <div class="card">
      <div  v-if="this.table.no" >
        <div style="display: flex">
          <div style="flex:1">
            餐桌号 {{this.table.no}} 开始点餐
          </div>
          <el-button type= "primary" @click="removeTable()">退桌</el-button>
        </div>
      </div>
      <div style="color: #666;" v-else>
      您还未选餐桌， 请先 <a href="/home">选择餐桌</a> 再点餐
    </div>
    </div>






</template>

<script>
import router from "@/router";

export default {
  name: "Order",
  data(){
    return {
      tableId: router.currentRoute.query.tableId,
      table: {}
    }
  },
  methods: {
    loadTable(){
      this.$request.get('/tables/selectById/' + this.tableId).then(res => {
        this.table = res.data || {}
      })
    },
    removeTable(){
      this.table.free="否"
      this.$request.put('/tables/update', this.table).then(res => {
        console.log(res)
        if (res.code === '200'){
          this.$router.push('/home');
        } else {
          this.$alert(res.msg);
        }
      })
    }
  },

  created() {
    this.loadTable();
  }

}
</script>

<style scoped>

</style>