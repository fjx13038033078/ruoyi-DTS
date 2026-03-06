<template>
  <div class="pay-success">
    <div class="success-content">
      <div class="success-icon">
        <i class="el-icon-success"></i>
      </div>
      <h2 class="success-title">支付成功！</h2>
      <div class="success-actions">
        <el-button type="primary" @click="$router.push('/front/profile')">
          查看我的订单
        </el-button>
        <el-button plain @click="$router.push('/front')">
          返回首页
        </el-button>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'PaySuccess'
}
</script>

<style lang="scss" scoped>
.pay-success {
  min-height: 60vh;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 40px;
}

.success-content {
  text-align: center;
}

.success-icon {
  margin-bottom: 24px;
  i {
    font-size: 80px;
    color: #67c23a;
  }
}

.success-title {
  font-size: 24px;
  color: #333;
  margin: 0 0 32px 0;
  font-weight: 500;
}

.success-actions {
  display: flex;
  gap: 16px;
  justify-content: center;
  flex-wrap: wrap;
}
</style>
