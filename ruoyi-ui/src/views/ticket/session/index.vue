<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="80px">
      <el-form-item label="演出ID" prop="performanceId">
        <el-input
          v-model="queryParams.performanceId"
          placeholder="请输入演出ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="场馆名称" prop="venueName">
        <el-input
          v-model="queryParams.venueName"
          placeholder="请输入场馆名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择" clearable>
          <el-option label="未开始" value="0" />
          <el-option label="售票中" value="1" />
          <el-option label="已结束" value="2" />
        </el-select>
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
          v-hasPermi="['ticket:session:add']"
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
          v-hasPermi="['ticket:session:edit']"
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
          v-hasPermi="['ticket:session:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['ticket:session:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="sessionList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="场次ID" align="center" prop="sessionId" width="80" />
      <el-table-column label="演出ID" align="center" prop="performanceId" width="80" />
      <el-table-column label="演出场馆" align="center" prop="venueName" :show-overflow-tooltip="true" />
      <el-table-column label="开始时间" align="center" prop="startTime" width="160">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.startTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="售票开启时间" align="center" prop="saleStartTime" width="160">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.saleStartTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center" prop="status" width="90">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.status === '0'" type="info">未开始</el-tag>
          <el-tag v-else-if="scope.row.status === '1'" type="success">售票中</el-tag>
          <el-tag v-else type="danger">已结束</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="200">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-setting"
            @click="handleSeatConfig(scope.row)"
            v-hasPermi="['ticket:session:edit']"
          >座位配置</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['ticket:session:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['ticket:session:remove']"
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

    <!-- 添加或修改场次对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="110px">
        <el-form-item label="关联演出" prop="performanceId">
          <el-select v-model="form.performanceId" placeholder="请选择演出" filterable style="width: 100%">
            <el-option
              v-for="item in performanceOptions"
              :key="item.performanceId"
              :label="item.title"
              :value="item.performanceId"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="演出场馆" prop="venueName">
          <el-input v-model="form.venueName" placeholder="请输入演出场馆" />
        </el-form-item>
        <el-form-item label="演出开始时间" prop="startTime">
          <el-date-picker
            v-model="form.startTime"
            type="datetime"
            value-format="yyyy-MM-dd HH:mm:ss"
            placeholder="选择开始时间"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="售票开启时间" prop="saleStartTime">
          <el-date-picker
            v-model="form.saleStartTime"
            type="datetime"
            value-format="yyyy-MM-dd HH:mm:ss"
            placeholder="选择售票开启时间"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio label="0">未开始</el-radio>
            <el-radio label="1">售票中</el-radio>
            <el-radio label="2">已结束</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 座位配置弹窗 -->
    <el-dialog title="座位配置" :visible.sync="seatConfigOpen" width="450px" append-to-body>
      <el-form ref="seatForm" :model="seatForm" :rules="seatRules" label-width="100px">
        <el-form-item label="场次" prop="sessionId">
          <el-input v-model="seatForm.sessionName" disabled />
        </el-form-item>
        <el-form-item label="区域名称" prop="areaName">
          <el-input v-model="seatForm.areaName" placeholder="如：A区、VIP区" />
        </el-form-item>
        <el-form-item label="统一票价" prop="price">
          <el-input-number v-model="seatForm.price" :min="0" :precision="2" placeholder="票价" style="width: 100%" />
        </el-form-item>
        <el-form-item label="排数" prop="rows">
          <el-input-number v-model="seatForm.rows" :min="1" :max="50" placeholder="排数" style="width: 100%" />
        </el-form-item>
        <el-form-item label="每排座位数" prop="cols">
          <el-input-number v-model="seatForm.cols" :min="1" :max="100" placeholder="每排座位数" style="width: 100%" />
        </el-form-item>
        <el-alert
          title="将按「X排Y座」格式批量生成座位，如：1排1座、1排2座..."
          type="info"
          :closable="false"
          show-icon
        />
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" :loading="seatSubmitLoading" @click="submitSeatConfig">生成座位</el-button>
        <el-button @click="seatConfigOpen = false">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listSession, getSession, delSession, addSession, updateSession, batchGenerateSeats } from "@/api/ticket/session";
import { listPerformance } from "@/api/ticket/performance";

export default {
  name: "Session",
  data() {
    return {
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      sessionList: [],
      performanceOptions: [],
      title: "",
      open: false,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        performanceId: undefined,
        venueName: undefined,
        status: undefined
      },
      form: {},
      rules: {
        performanceId: [
          { required: true, message: "请选择关联演出", trigger: "change" }
        ],
        venueName: [
          { required: true, message: "请输入演出场馆", trigger: "blur" }
        ],
        startTime: [
          { required: true, message: "请选择演出开始时间", trigger: "change" }
        ],
        saleStartTime: [
          { required: true, message: "请选择售票开启时间", trigger: "change" }
        ]
      },
      // 座位配置
      seatConfigOpen: false,
      seatSubmitLoading: false,
      seatForm: {
        sessionId: undefined,
        sessionName: "",
        areaName: "",
        price: 0,
        rows: 10,
        cols: 20
      },
      seatRules: {
        areaName: [
          { required: true, message: "请输入区域名称", trigger: "blur" }
        ],
        price: [
          { required: true, message: "请输入票价", trigger: "blur" }
        ],
        rows: [
          { required: true, message: "请输入排数", trigger: "blur" },
          { type: "number", min: 1, max: 50, message: "排数范围为 1-50", trigger: "blur" }
        ],
        cols: [
          { required: true, message: "请输入每排座位数", trigger: "blur" },
          { type: "number", min: 1, max: 100, message: "每排座位数范围为 1-100", trigger: "blur" }
        ]
      }
    };
  },
  created() {
    this.getList();
    this.getPerformanceList();
  },
  methods: {
    getList() {
      this.loading = true;
      listSession(this.queryParams).then(response => {
        this.sessionList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    getPerformanceList() {
      listPerformance({ pageNum: 1, pageSize: 1000 }).then(response => {
        this.performanceOptions = response.rows || [];
      });
    },
    cancel() {
      this.open = false;
      this.reset();
    },
    reset() {
      this.form = {
        sessionId: undefined,
        performanceId: undefined,
        venueName: undefined,
        startTime: undefined,
        saleStartTime: undefined,
        status: "1"
      };
      this.resetForm("form");
    },
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.sessionId);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加场次";
    },
    handleUpdate(row) {
      this.reset();
      const sessionId = row.sessionId || this.ids[0];
      getSession(sessionId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改场次";
      });
    },
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.sessionId != undefined) {
            updateSession(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addSession(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    handleDelete(row) {
      const sessionIds = row.sessionId ? [row.sessionId] : this.ids;
      this.$modal.confirm('是否确认删除所选场次数据？').then(() => {
        return delSession(sessionIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    handleExport() {
      this.download('ticket/session/export', {
        ...this.queryParams
      }, `session_${new Date().getTime()}.xlsx`);
    },
    /** 座位配置 */
    handleSeatConfig(row) {
      this.seatForm = {
        sessionId: row.sessionId,
        sessionName: `${row.venueName} - ${this.parseTime(row.startTime, '{y}-{m}-{d} {h}:{i}')}`,
        areaName: "",
        price: 0,
        rows: 10,
        cols: 20
      };
      this.seatConfigOpen = true;
      this.$nextTick(() => {
        this.$refs["seatForm"] && this.$refs["seatForm"].clearValidate();
      });
    },
    submitSeatConfig() {
      this.$refs["seatForm"].validate(valid => {
        if (valid) {
          this.seatSubmitLoading = true;
          batchGenerateSeats({
            sessionId: this.seatForm.sessionId,
            areaName: this.seatForm.areaName,
            price: this.seatForm.price,
            rows: this.seatForm.rows,
            cols: this.seatForm.cols
          }).then(response => {
            this.$modal.msgSuccess(response.msg || "座位生成成功");
            this.seatConfigOpen = false;
            this.getList(); // 提交后刷新场次列表
            this.seatSubmitLoading = false;
          }).catch(() => {
            this.seatSubmitLoading = false;
          });
        }
      });
    }
  }
};
</script>
