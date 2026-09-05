document.addEventListener('click', function (event) {
  var button = event.target.closest('[data-password-toggle]');
  if (!button) return;
  var input = document.getElementById(button.getAttribute('data-password-toggle'));
  if (!input) return;
  input.type = input.type === 'password' ? 'text' : 'password';
});
