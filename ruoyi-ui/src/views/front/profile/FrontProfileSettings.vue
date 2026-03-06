<template>
  <div class="front-settings">
    <el-card class="settings-card">
      <div slot="header" class="card-header">
        <el-button type="text" icon="el-icon-arrow-left" @click="$router.push('/front/profile')" class="back-btn">
          返回个人中心
        </el-button>
        <span><i class="el-icon-setting"></i> 账号设置</span>
      </div>
      <div class="settings-content" v-if="isLogin">
        <div class="avatar-section text-center">
          <userAvatar />
        </div>
        <el-tabs v-model="activeTab" class="settings-tabs">
          <el-tab-pane label="基本资料" name="userinfo">
            <el-form ref="userForm" :model="form" :rules="userRules" label-width="100px" class="settings-form">
              <el-form-item label="用户昵称" prop="nickName">
                <el-input v-model="form.nickName" maxlength="30" placeholder="请输入昵称" />
              </el-form-item>
              <el-form-item label="手机号码" prop="phonenumber">
                <el-input v-model="form.phonenumber" maxlength="11" placeholder="请输入手机号" />
              </el-form-item>
              <el-form-item label="邮箱" prop="email">
                <el-input v-model="form.email" maxlength="50" placeholder="请输入邮箱" />
              </el-form-item>
              <el-form-item label="性别">
                <el-radio-group v-model="form.sex">
                  <el-radio label="0">男</el-radio>
                  <el-radio label="1">女</el-radio>
                </el-radio-group>
              </el-form-item>
              <el-form-item>
                <el-button type="primary" @click="submitUserInfo">保存</el-button>
              </el-form-item>
            </el-form>
          </el-tab-pane>
          <el-tab-pane label="修改密码" name="resetPwd">
            <el-form ref="pwdForm" :model="pwdForm" :rules="pwdRules" label-width="100px" class="settings-form">
              <el-form-item label="旧密码" prop="oldPassword">
                <el-input v-model="pwdForm.oldPassword" placeholder="请输入旧密码" type="password" show-password />
              </el-form-item>
              <el-form-item label="新密码" prop="newPassword">
                <el-input v-model="pwdForm.newPassword" placeholder="请输入新密码" type="password" show-password />
              </el-form-item>
              <el-form-item label="确认密码" prop="confirmPassword">
                <el-input v-model="pwdForm.confirmPassword" placeholder="请确认新密码" type="password" show-password />
              </el-form-item>
              <el-form-item>
                <el-button type="primary" @click="submitPwd">保存</el-button>
              </el-form-item>
            </el-form>
          </el-tab-pane>
        </el-tabs>
      </div>
      <div v-else class="login-tip">
        <p>请先 <router-link to="/login">登录</router-link></p>
      </div>
    </el-card>
  </div>
</template>

<script>
import userAvatar from '@/views/system/user/profile/userAvatar'
import { getUserProfile, updateUserProfile, updateUserPwd } from '@/api/system/user'

export default {
  name: 'FrontProfileSettings',
  components: { userAvatar },
  data() {
    const equalToPassword = (rule, value, callback) => {
      if (this.pwdForm.newPassword !== value) {
        callback(new Error('两次输入的密码不一致'))
      } else {
        callback()
      }
    }
    return {
      activeTab: 'userinfo',
      form: {
        nickName: '',
        phonenumber: '',
        email: '',
        sex: undefined
      },
      userRules: {
        nickName: [{ required: true, message: '用户昵称不能为空', trigger: 'blur' }],
        email: [
          { required: true, message: '邮箱地址不能为空', trigger: 'blur' },
          { type: 'email', message: '请输入正确的邮箱地址', trigger: ['blur', 'change'] }
        ],
        phonenumber: [
          { required: true, message: '手机号码不能为空', trigger: 'blur' },
          { pattern: /^1[3|4|5|6|7|8|9][0-9]\d{8}$/, message: '请输入正确的手机号码', trigger: 'blur' }
        ]
      },
      pwdForm: {
        oldPassword: undefined,
        newPassword: undefined,
        confirmPassword: undefined
      },
      pwdRules: {
        oldPassword: [{ required: true, message: '旧密码不能为空', trigger: 'blur' }],
        newPassword: [
          { required: true, message: '新密码不能为空', trigger: 'blur' },
          { min: 6, max: 20, message: '长度在 6 到 20 个字符', trigger: 'blur' }
        ],
        confirmPassword: [
          { required: true, message: '确认密码不能为空', trigger: 'blur' },
          { required: true, validator: equalToPassword, trigger: 'blur' }
        ]
      }
    }
  },
  computed: {
    isLogin() {
      return this.$store.getters.token
    }
  },
  created() {
    if (this.isLogin) {
      this.loadUser()
    }
  },
  methods: {
    loadUser() {
      getUserProfile().then(res => {
        const user = res.data
        if (user) {
          this.form = {
            nickName: user.nickName,
            phonenumber: user.phonenumber,
            email: user.email,
            sex: user.sex
          }
        }
      })
    },
    submitUserInfo() {
      this.$refs.userForm.validate(valid => {
        if (valid) {
          updateUserProfile(this.form).then(() => {
            this.$message.success('修改成功')
          })
        }
      })
    },
    submitPwd() {
      this.$refs.pwdForm.validate(valid => {
        if (valid) {
          updateUserPwd(this.pwdForm.oldPassword, this.pwdForm.newPassword).then(() => {
            this.$message.success('修改成功')
            this.pwdForm = { oldPassword: undefined, newPassword: undefined, confirmPassword: undefined }
          })
        }
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.front-settings {
  .settings-card {
    max-width: 600px;
    margin: 0 auto;
  }
  .card-header {
    display: flex;
    align-items: center;
    gap: 16px;
    font-size: 18px;
    font-weight: 500;
    .back-btn {
      padding: 0;
      color: #606266;
      font-size: 14px;
      &:hover {
        color: #409eff;
      }
    }
  }
  .avatar-section {
    margin-bottom: 24px;
    padding-bottom: 24px;
    border-bottom: 1px solid #ebeef5;
  }
  .settings-tabs {
    ::v-deep .el-tabs__header {
      margin-bottom: 20px;
    }
  }
  .settings-form {
    max-width: 400px;
  }
  .login-tip {
    text-align: center;
    padding: 40px;
    color: #666;
    a {
      color: #409eff;
    }
  }
}
</style>
