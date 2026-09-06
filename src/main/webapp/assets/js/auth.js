document.addEventListener('click', function (event) {
  var button = event.target.closest('[data-password-toggle]');
  if (!button) return;
  var input = document.getElementById(button.getAttribute('data-password-toggle'));
  if (!input) return;
  input.type = input.type === 'password' ? 'text' : 'password';
});

document.querySelectorAll('[data-otp-input]').forEach(function (input) {
  input.addEventListener('input', function () { input.value = input.value.replace(/\D/g, '').slice(0, 6); });
});

document.querySelectorAll('[data-resend-countdown]').forEach(function (button) {
  var seconds = 60;
  button.disabled = true;
  var timer = window.setInterval(function () {
    seconds -= 1;
    button.textContent = seconds > 0 ? 'Gửi lại OTP (' + seconds + 's)' : 'Gửi lại OTP';
    if (seconds <= 0) { button.disabled = false; window.clearInterval(timer); }
  }, 1000);
});
