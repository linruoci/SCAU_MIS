<template>
    <div>
      <div class="card">
        <div v-if="this.table.no" >
          <div style="display: flex">
            <div style="flex:1">
              餐桌号 {{this.table.no}} 开始点餐
            </div>
            <el-button type= "primary" @click="removeTable()">退桌</el-button>
          </div>
        </div>
        <div style="color: #666;" v-else>
          您还未选餐桌， 请先 <a href="/tablesManager">选择餐桌</a> 再点餐
        </div>
      </div>

      <div class="table" v-if="this.table.no">
        <el-table :data="tableData" strip @selection-change="handleSelectionChange">
          <el-table-column type="selection" width="55" align="center"></el-table-column>
          <el-table-column prop="id" label="序号" width="70" align="center" sortable></el-table-column>
          <el-table-column prop="name" label="名称"></el-table-column>
          <el-table-column prop="price" label="价格"></el-table-column>
          <el-table-column prop="discount" label="折扣"></el-table-column>
          <el-table-column prop="img" label="图片">
            <template v-slot="scope">
              <div style="display: flex; align-items: center">
                <el-image style="width: 40px; height: 40px;" v-if="scope.row.img"
                          :src="scope.row.img" :preview-src-list="[scope.row.img]"></el-image>
              </div>
            </template>
          </el-table-column>
          <el-table-column prop="descr" label="描述"></el-table-column>
          <el-table-column prop="taste" label="口味"></el-table-column>
          <el-table-column prop="specs" label="规格"></el-table-column>
          <el-table-column prop="date" label="上架日期"></el-table-column>
          <el-table-column prop="status" label="上架状态">
            <template v-slot="scope">
              <el-tag type="success" v-if="scope.row.status === '上架'">上架</el-tag>
              <el-tag type="warning" v-if="scope.row.status === '下架'">下架</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="categoryName" label="分类"></el-table-column>
          <el-table-column label="操作" align="center" width="180">
          <template v-slot="scope">
            <!--              <el-button size="mini" type="primary" plain @click="handleEdit(scope.row)">编辑</el-button>-->
            <!--              <el-button size="mini" type="danger" plain @click="del(scope.row.id)">删除</el-button>-->
            <el-input-number v-model="scope.row.num" @change="handleChange" :min="0" :max="10" label="描述文字"></el-input-number>
          </template>
        </el-table-column>
        </el-table>

        <div class="pagination">
          <el-pagination
              background
              @current-change="handleCurrentChange"
              :current-page="pageNum"
              :page-sizes="[5, 10, 20]"
              :page-size="pageSize"
              layout="total, prev, pager, next"
              :total="total">
          </el-pagination>
        </div>

      </div>


      <div class="card" v-if="this.table.no" >
        <div style="display: flex">
          <div style="flex:1">
            <p>总金额: {{ totalAmount }}</p>
          </div>
          <el-button type= "primary" @click="payMyGoods()">结算</el-button>
        </div>
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
      table: {},
      tableData: [],  // 所有的数据
      categoryList: [],
      pageNum: 1,   // 当前的页码
      pageSize: 10,  // 每页显示的个数
      total: 0,
      name: null,
      fromVisible: false,
      form: {},
      user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
      rules: {
        name: [
          {required: true, message: '请输入名称', trigger: 'blur'},
        ],
        price: [
          {required: true, message: '请输入名价格', trigger: 'blur'},
        ]
      },
      ids: [],
      goods: [],
      num: 0,
    }
  },
  computed: {
    totalAmount() {
      let total = 0;
      for (let item of this.tableData) {
        // 检查商品数量是否合法，如果非法则置为 0
        const num = isNaN(item.num) ? 0 : item.num;
        total += item.price * num * item.discount;
      }
      return total.toFixed(1); // 保留一位小数
    },
    selectedGoods() {
      return this.tableData.filter(good => good.num > 0);
    }
  },
  methods: {
    handleChange(value) {
      console.log(value);
    },
    loadTable(){
      this.$request.get('/tables/selectById/' + this.tableId).then(res => {
        this.table = res.data || {}
      })
    },
    removeTable(){
      this.table.free="是"
      this.$request.put('/tables/update', this.table).then(res => {
        console.log(res)
        if (res.code === '200'){
          this.$router.push('/home');
        } else {
          this.$alert(res.msg);
        }
      })
    },
    payMyGoods(){
      const orderData = this.selectedGoods.map(good => {
        return {
          goodsId: good.id,
          nums: good.num
        };
      });
      this.$request.post('/orders/addMyOrder', orderData).then(res => {
        alert("订单提交成功!");
        window.open('http://localhost:8080/orders')
      })
    },
    handleAdd() {   // 新增数据
      this.form = {}  // 新增数据的时候清空数据
      this.fromVisible = true   // 打开弹窗
    },
    handleEdit(row) {   // 编辑数据
      this.form = JSON.parse(JSON.stringify(row))  // 给form对象赋值  注意要深拷贝数据
      this.fromVisible = true   // 打开弹窗
    },
    save() {   // 保存按钮触发的逻辑  它会触发新增或者更新
      this.$refs.formRef.validate((valid) => {
        if (valid) {
          this.$request({
            url: this.form.id ? '/goods/update' : '/goods/add',
            method: this.form.id ? 'PUT' : 'POST',
            data: this.form
          }).then(res => {
            if (res.code === '200') {  // 表示成功保存
              this.$message.success('保存成功')
              this.load(1)
              this.fromVisible = false
            } else {
              this.$message.error(res.msg)  // 弹出错误的信息
            }
          })
        }
      })
    },
    del(id) {   // 单个删除
      this.$confirm('您确定删除吗？', '确认删除', {type: "warning"}).then(response => {
        this.$request.delete('/goods/delete/' + id).then(res => {
          if (res.code === '200') {   // 表示操作成功
            this.$message.success('操作成功')
            this.load(1)
          } else {
            this.$message.error(res.msg)  // 弹出错误的信息
          }
        })
      }).catch(() => {
      })
    },
    handleSelectionChange(rows) {   // 当前选中的所有的行数据
      this.ids = rows.map(v => v.id)
      this.goods = rows
      console.log(this.goods)
    },
    delBatch() {   // 批量删除
      if (!this.ids.length) {
        this.$message.warning('请选择数据')
        return
      }
      this.$confirm('您确定批量删除这些数据吗？', '确认删除', {type: "warning"}).then(response => {
        this.$request.delete('/goods/delete/batch', {data: this.ids}).then(res => {
          if (res.code === '200') {   // 表示操作成功
            this.$message.success('操作成功')
            this.load(1)
          } else {
            this.$message.error(res.msg)  // 弹出错误的信息
          }
        })
      }).catch(() => {
      })
    },
    load(pageNum) {  // 分页查询
      if (pageNum) this.pageNum = pageNum
      this.$request.get('/goods/selectAllExceptStatus', {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          username: this.username,
          name: this.name,
        }
      }).then(res => {
        this.tableData = res.data?.list
        this.total = res.data?.total
      })
    },
    reset() {
      this.username = null
      this.name = null
      this.load(1)
    },
    handleCurrentChange(pageNum) {
      this.load(pageNum)
    },
    handleFileSuccess(response, file, fileList) {
      // 把属性换成上传的图片的链接
      this.form.img = response.data
    }
  },

  created() {
    this.loadTable();

    this.load(1)
    let businessId = this.user.role === 'ADMIN' ? null : this.user.id
    this.$request.get('/category/selectAll', {
      params: {
        businessId: businessId
      }
    }).then(res => {
      this.categoryList = res.data
    })
  }

}
</script>

<style scoped>

</style>