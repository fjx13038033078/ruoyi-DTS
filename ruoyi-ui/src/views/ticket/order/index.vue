<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="80px">
      <el-form-item label="订单编号" prop="orderNo">
        <el-input
          v-model="queryParams.orderNo"
          placeholder="请输入订单编号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="用户ID" prop="userId">
        <el-input
          v-model="queryParams.userId"
          placeholder="请输入用户ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="场次ID" prop="sessionId">
        <el-input
          v-model="queryParams.sessionId"
          placeholder="请输入场次ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="订单状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择" clearable>
          <el-option label="待支付" value="0" />
          <el-option label="已支付" value="1" />
          <el-option label="已取消" value="2" />
          <el-option label="退款处理中" value="3" />
          <el-option label="已退款" value="4" />
        </el-select>
      </el-form-item>
      <el-form-item label="下单时间">
        <el-date-picker
          v-model="dateRange"
          style="width: 240px"
          value-format="yyyy-MM-dd"
          type="daterange"
          range-separator="-"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['ticket:order:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['ticket:order:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['ticket:order:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['ticket:order:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="orderList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="订单ID" align="center" prop="orderId" width="80" />
      <el-table-column label="订单编号" align="center" prop="orderNo" width="180" :show-overflow-tooltip="true" />
      <el-table-column label="用户ID" align="center" prop="userId" width="80" />
      <el-table-column label="场次ID" align="center" prop="sessionId" width="80" />
      <el-table-column label="订单金额" align="center" prop="totalAmount" width="100">
        <template slot-scope="scope">
          <span>¥{{ scope.row.totalAmount }}</span>
        </template>
      </el-table-column>
      <el-table-column label="订单状态" align="center" prop="status" width="100">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.status === '0'" type="warning">待支付</el-tag>
          <el-tag v-else-if="scope.row.status === '1'" type="success">已支付</el-tag>
          <el-tag v-else-if="scope.row.status === '2'" type="info">已取消</el-tag>
          <el-tag v-else-if="scope.row.status === '3'" type="warning">退款处理中</el-tag>
          <el-tag v-else type="danger">已退款</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="下单时间" align="center" prop="createTime" width="160">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="支付时间" align="center" prop="payTime" width="160">
        <template slot-scope="scope">
          <span>{{ scope.row.payTime ? parseTime(scope.row.payTime) : '-' }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="150">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['ticket:order:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['ticket:order:remove']"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改订单对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="550px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="订单编号" prop="orderNo">
          <el-input v-model="form.orderNo" placeholder="请输入订单编号" />
        </el-form-item>
        <el-form-item label="用户ID" prop="userId">
          <el-input-number v-model="form.userId" :min="1" placeholder="用户ID" style="width: 100%" />
        </el-form-item>
        <el-form-item label="场次ID" prop="sessionId">
          <el-input-number v-model="form.sessionId" :min="1" placeholder="场次ID" style="width: 100%" />
        </el-form-item>
        <el-form-item label="订单金额" prop="totalAmount">
          <el-input-number v-model="form.totalAmount" :min="0" :precision="2" placeholder="订单金额" style="width: 100%" />
        </el-form-item>
        <el-form-item label="订单状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio label="0">待支付</el-radio>
            <el-radio label="1">已支付</el-radio>
            <el-radio label="2">已取消</el-radio>
            <el-radio label="3">退款处理中</el-radio>
            <el-radio label="4">已退款</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="交易流水号" prop="transactionId">
          <el-input v-model="form.transactionId" placeholder="支付宝/微信交易流水号" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listOrder, getOrder, delOrder, addOrder, updateOrder } from "@/api/ticket/order";

export default {
  name: "Order",
  data() {
    return {
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      orderList: [],
      title: "",
      open: false,
      dateRange: [],
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        orderNo: undefined,
        userId: undefined,
        sessionId: undefined,
        status: undefined
      },
      form: {},
      rules: {
        orderNo: [
          { required: true, message: "订单编号不能为空", trigger: "blur" }
        ],
        userId: [
          { required: true, message: "用户ID不能为空", trigger: "blur" }
        ],
        sessionId: [
          { required: true, message: "场次ID不能为空", trigger: "blur" }
        ],
        totalAmount: [
          { required: true, message: "订单金额不能为空", trigger: "blur" }
        ]
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    getList() {
      this.loading = true;
      listOrder(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
        this.orderList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    cancel() {
      this.open = false;
      this.reset();
    },
    reset() {
      this.form = {
        orderId: undefined,
        orderNo: undefined,
        userId: undefined,
        sessionId: undefined,
        totalAmount: undefined,
        status: "0",
        transactionId: undefined,
        payTime: undefined
      };
      this.resetForm("form");
    },
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    resetQuery() {
      this.dateRange = [];
      this.resetForm("queryForm");
      this.handleQuery();
    },
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.orderId);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加订单";
    },
    handleUpdate(row) {
      this.reset();
      const orderId = row.orderId || this.ids[0];
      getOrder(orderId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改订单";
      });
    },
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.orderId != undefined) {
            updateOrder(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addOrder(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    handleDelete(row) {
      const orderIds = row.orderId ? [row.orderId] : this.ids;
      this.$modal.confirm('是否确认删除所选订单数据？').then(() => {
        return delOrder(orderIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    handleExport() {
      this.download('ticket/order/export', {
        ...this.addDateRange(this.queryParams, this.dateRange)
      }, `order_${new Date().getTime()}.xlsx`);
    }
  }
};
</script>
