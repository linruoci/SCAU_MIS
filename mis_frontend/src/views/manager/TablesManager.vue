<template>
  <div class = "card">
    <div style="display: flex; flex-wrap: wrap">
      <div v-for="item in tables" :key="item.id" style="color: #666; text-align: center; margin-right: 20px; margin-bottom: 20px" >
        <div> <img src="@/assets/imgs/点餐.png" alt="" style="width: 80px"></div>
        <div>{{item.no}}</div>
        <div style="font-size: 12px; margin: 10px 0">{{item.unit}}可用餐</div>
        <div style="margin: 10px 0">
          <span style="color: #3BB201" v-if="item.free === '是'">空闲</span>
          <span style="color: #b20130" v-if="item.free === '否'">占用</span>
        </div>
        <div v-if="item.free === '是'">
          <el-button type="primary" @click="addOrder(item)">开始点餐</el-button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'TablesManager',
  data() {
    return {
      user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
      tables:[]
    }
  },
  methods:{
    addOrder(item) {
      item.free="是"
      this.$request.put('/tables/update', item).then(res => {
        console.log(res)
        if (res.code === '200'){
          this.$router.push('/order?tableId=' + item.id)
        } else {
          this.$alert(res.msg);
        }
      })
    },
  },
  created() {
    this.$request.get('/tables/selectAll').then(res => {
      this.tables = res.data || []
    });
  }
}
</script>

<style scoped>

</style>