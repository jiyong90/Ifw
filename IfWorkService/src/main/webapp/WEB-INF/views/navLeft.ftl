<nav id="sidebar">
    <div class="sidebar-header">
        <a href="#">
            <h3 class="full-logo CTAs"><img src="${rc.getContextPath()}/soldev/img/logo_ko_60px.png" alt=""></h3>
            <strong class="logo text-center"><img src="${rc.getContextPath()}/soldev/img/gnb_logo.png" alt=""></strong>
        </a>
    </div>

    <ul class="list-unstyled components">
        <li>
            <a href="#">
                <i class="fas fa-user-cog"></i>
                <span>나의 정보</span>
            </a>
        </li>
        <li class="active">
            <a href="#homeSubmenu" data-toggle="collapse" aria-expanded="false" class="dropdown-toggle">
                <i class="fas fa-home"></i>
                <span>Home</span>
            </a>
            <ul class="collapse list-unstyled" id="homeSubmenu">
                <li>
                    <a href="#"><span>Home 1</span></a>
                </li>
                <li>
                    <a href="#"><span>Home 2</span></a>
                </li>
                <li>
                    <a href="#"><span>Home 3</span></a>
                </li>
            </ul>
        </li>
        <li>
            <a href="#">
                <i class="fas fa-briefcase"></i>
                <span>About</span>
            </a>
            <a href="#pageSubmenu" data-toggle="collapse" aria-expanded="false" class="dropdown-toggle">
                <i class="fas fa-copy"></i>
                <span>Pages</span>
            </a>
            <ul class="collapse list-unstyled" id="pageSubmenu">
                <li>
                    <a href="${rc.getContextPath()}/${tsId}/views/workTimeCalendar"><span>유연근무제 신청</span></a>
                </li>
                <li>
                    <a href="#"><span>Page 2</span></a>
                </li>
                <li>
                    <a href="#"><span>Page 3</span></a>
                </li>
            </ul>
        </li>
        
        <li>
            <a href="#">
                <i class="fas fa-question"></i>
                <span>FAQ</span>
            </a>
        </li>
        <li>
            <a href="#">
                <i class="fas fa-paper-plane"></i>
                <span>Contact</span>
            </a>
        </li>
    </ul>

        <!-- <ul class="list-unstyled CTAs">
    <li>
        <a href="https://bootstrapious.com/tutorial/files/sidebar.zip" class="download">Download source</a>
    </li>
    <li>
        <a href="https://bootstrapious.com/p/bootstrap-sidebar" class="article">Back to article</a>
    </li>
    </ul> -->
</nav>