<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="80px">
      <el-form-item label="演出标题" prop="title">
        <el-input
          v-model="queryParams.title"
          placeholder="请输入演出标题"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="是否推荐" prop="isRecommend">
        <el-select v-model="queryParams.isRecommend" placeholder="请选择" clearable>
          <el-option label="是" value="1" />
          <el-option label="否" value="0" />
        </el-select>
      </el-form-item>
      <el-form-item label="上架状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择" clearable>
          <el-option label="上架" value="1" />
          <el-option label="下架" value="0" />
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
          v-hasPermi="['ticket:performance:add']"
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
          v-hasPermi="['ticket:performance:edit']"
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
          v-hasPermi="['ticket:performance:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['ticket:performance:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="performanceList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="演出ID" align="center" prop="performanceId" width="80" />
      <el-table-column label="演出标题" align="center" prop="title" :show-overflow-tooltip="true" />
      <el-table-column label="海报" align="center" prop="posterUrl" width="100">
        <template slot-scope="scope">
          <image-preview v-if="scope.row.posterUrl" :src="scope.row.posterUrl" :width="50" :height="50" />
          <span v-else>-</span>
        </template>
      </el-table-column>
      <el-table-column label="是否推荐" align="center" prop="isRecommend" width="90">
        <template slot-scope="scope">
          <el-tag :type="scope.row.isRecommend === '1' ? 'success' : 'info'">
            {{ scope.row.isRecommend === '1' ? '是' : '否' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="上架状态" align="center" prop="status" width="90">
        <template slot-scope="scope">
          <el-tag :type="scope.row.status === '1' ? 'success' : 'danger'">
            {{ scope.row.status === '1' ? '上架' : '下架' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" width="160">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="150">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['ticket:performance:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['ticket:performance:remove']"
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

    <!-- 添加或修改演出对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="演出标题" prop="title">
          <el-input v-model="form.title" placeholder="请输入演出标题" />
        </el-form-item>
        <el-form-item label="海报图片" prop="posterUrl">
          <image-upload v-model="form.posterUrl" :limit="1" />
        </el-form-item>
        <el-form-item label="详情描述" prop="description">
          <el-input v-model="form.description" type="textarea" :rows="4" placeholder="请输入详情描述" />
        </el-form-item>
        <el-form-item label="是否推荐" prop="isRecommend">
          <el-radio-group v-model="form.isRecommend">
            <el-radio label="0">否</el-radio>
            <el-radio label="1">是</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="上架状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio label="0">下架</el-radio>
            <el-radio label="1">上架</el-radio>
          </el-radio-group>
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
import { listPerformance, getPerformance, delPerformance, addPerformance, updatePerformance } from "@/api/ticket/performance";

export default {
  name: "Performance",
  data() {
    return {
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      performanceList: [],
      title: "",
      open: false,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        title: undefined,
        isRecommend: undefined,
        status: undefined
      },
      form: {},
      rules: {
        title: [
          { required: true, message: "演出标题不能为空", trigger: "blur" }
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
      listPerformance(this.queryParams).then(response => {
        this.performanceList = response.rows;
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
        performanceId: undefined,
        title: undefined,
        posterUrl: undefined,
        description: undefined,
        isRecommend: "0",
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
      this.ids = selection.map(item => item.performanceId);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加演出";
    },
    handleUpdate(row) {
      this.reset();
      const performanceId = row.performanceId || this.ids[0];
      getPerformance(performanceId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改演出";
      });
    },
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.performanceId != undefined) {
            updatePerformance(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addPerformance(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    handleDelete(row) {
      const performanceIds = row.performanceId ? [row.performanceId] : this.ids;
      this.$modal.confirm('是否确认删除所选演出数据？').then(() => {
        return delPerformance(performanceIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    handleExport() {
      this.download('ticket/performance/export', {
        ...this.queryParams
      }, `performance_${new Date().getTime()}.xlsx`);
    }
  }
};
</script>
